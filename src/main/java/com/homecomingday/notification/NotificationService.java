package com.homecomingday.notification;

import com.amazonaws.services.kms.model.NotFoundException;
import com.homecomingday.notification.requestDto.NotificationCountDto;
import com.homecomingday.notification.responseDto.NotificationResponseDto;
import com.homecomingday.domain.*;
import com.homecomingday.notification.EmitterRepository;
import com.homecomingday.notification.EmitterRepositoryImpl;
import com.homecomingday.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationRepository notificationRepository;

//    public SseEmitter subscribe(Long userId, String lastEventId) {
//        //emitter 하나하나 에 고유의 값을 주기 위해
//        String emitterId = makeTimeIncludeId(userId);
//        System.out.println(emitterId);
//
//        Long timeout = 60L * 1000L * 60L; // 1시간
//        // 생성된 emiiterId를 기반으로 emitter를 저장
//        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
//
//        //emitter의 시간이 만료된 후 레포에서 삭제
//        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
//        // 503 에러를 방지하기 위해 처음 연결 진행 시 더미 데이터를 전달
//        String eventId = makeTimeIncludeId(userId);
//        // 수 많은 이벤트 들을 구분하기 위해 이벤트 ID에 시간을 통해 구분을 해줌
//        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");
//
//        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, userId, emitterId, emitter);
//        }
//        System.out.println(emitter);
//        return emitter;
//    }

    // SseEmitter를 구분 -> 구분자로 시간을 사용함
    // 시간을 붙혀주는 이유 -> 브라우저에서 여러개의 구독을 진행 시
    //탭 마다 SssEmitter 구분을 위해 시간을 붙여 구분하기 위해 아래와 같이 진행
//    private String makeTimeIncludeId(Long userId) {
//        return userId + "_" + System.currentTimeMillis();
//    }
//
//    // Last - event - id 가 존재한다는 것은 받지 못한 데이터가 있다는 것이다.
//    private boolean hasLostData(String lastEventId) {
//        return !lastEventId.isEmpty();
//    }
//    // 받지못한 데이터가 있다면 last - event - id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
//    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//    }


//    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
//    private void sendToClient(SseEmitter emitter, String id, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(id)
//                    .name("sse")
//                    .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(id);
//            throw new RuntimeException("see 연결 오류!");
//        }
//    }
    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
            try {
                emitter.send(SseEmitter.event()
                        .id(eventId)
                        .data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(emitterId);
                log.error("sse 연결오류!!!", exception);
            }
        }



    //댓글이나 좋아요 누르면 알림
    @Async
    public void send(Member receiver, NoticeType alarmType, Long articlesId, String title,String creatAt,Comment comment) {

//        여기 createdAt은 댓글 생성될때 찍히는시간,
        Notification notification = notificationRepository.save(createNotification(receiver, alarmType, articlesId, title, comment));
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.create(notification));
                }
        );
    }

    @Async
    public void send(Member receiver, NoticeType alarmType,  Long articlesId, String title,String creatAt,Heart heart) {

//        여기 createdAt은 댓글 생성될때 찍히는시간,
        Notification  notification = notificationRepository.save(createNotification(receiver, alarmType, articlesId, title, heart));
        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.create(notification));
                }
        );
    }


    private Notification createNotification(Member receiver, NoticeType noticeType,
                                            Long articlesId, String title,Comment comment) {

        return Notification.builder()
                .receiver(receiver)
                .noticeType(noticeType)
                .articlesId(articlesId)
                .title(title)
                .readState(false) // 현재 읽음상태
                .comment(comment)
                .build();
    }
    private Notification createNotification(Member receiver, NoticeType noticeType,
                                            Long articlesId, String title,Heart heart) {

        return Notification.builder()
                .receiver(receiver)
                .noticeType(noticeType)
                .articlesId(articlesId)
                .title(title)
                .readState(false) // 현재 읽음상태
                .heart(heart)
                .build();
    }

    //알림 전체 조회
    @Transactional
    public List<NotificationResponseDto> findAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiver_MemberId(userId);
        for (Notification notification : notifications) {
            notification.changeState();
        }
        return notifications.stream()
                .map(NotificationResponseDto::create)
                .collect(Collectors.toList());
    }


    //알림 전체 조회
//    @Transactional
//    public List<NotificationResponseDto> findAllNotifications(Long userId) {
//        System.out.println("1");
//        List<Notification> notifications = notificationRepository.findAllByReceiver_MemberId(userId);
//        System.out.println("2");
//        System.out.println(notifications);
//        List<NotificationResponseDto> notificationResponseDtoList = new ArrayList<>();
//        System.out.println("3");
//        for (Notification notification : notifications) {
//            notificationResponseDtoList.add(
//                    NotificationResponseDto.builder()
//                            .id(notification.getId())
//                            .noticeType(notification.getNoticeType())
//                            .articleId(notification.getUrl())
//                            .title(notification.getTitle())
//                            .readStatus(notification.getReadState())
//                            .createdAt(Time.convertLocaldatetimeToTime(notification.getCreatedAt()))
//                            .build()
//            );
//        }
//        System.out.println(notificationResponseDtoList);
//        System.out.println(notifications);
////        return notifications.stream()
////                .map(NotificationResponseDto::create)
////                .collect(Collectors.toList());
//        return notificationResponseDtoList;
//    }

    //알림 전체조회 후 읽음 처리
    @Transactional
    public void readNotification(Long notificationId) {

        //알림을 받은 사람의 id 와 알림의 id 를 받아와서 해당 알림을 찾는다.
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        Notification checkNotification = notification.orElseThrow(()-> new NotFoundException("사용자가 없습니다."));
        checkNotification.changeState(); // 읽음처리
    }


    //알림 카운트 - 구독자가 현재 읽지않은 알림 갯수
    @Transactional
    public NotificationCountDto countUnReadNotification(Long userId) {
        //유저의 알람리스트에서 ->readState(false)인 갯수를 측정 ,
        Long count = notificationRepository.countUnReadStateNotification(userId);
        return NotificationCountDto.builder()
                .count(count)
                .build();

    }


    //알림 전체 삭제
    @Transactional
    public void deleteAllByNotifications(UserDetailsImpl userDetails) {
        Long receiverId = userDetails.getMember().getId();
        notificationRepository.deleteAllByMember_Id(receiverId);

    }

    //단일 알림 삭제
    @Transactional
    public void deleteByNotifications(Long notificationId) {
        if (notificationId == null) {
            throw new NotFoundException("알림존재하지 않음");
        }
        notificationRepository.deleteById(notificationId);
    }



}
