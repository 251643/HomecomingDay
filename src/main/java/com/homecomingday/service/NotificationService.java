package com.homecomingday.service;

import com.homecomingday.controller.request.NotificationCountDto;
import com.homecomingday.controller.response.NotificationResponseDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.NoticeType;
import com.homecomingday.domain.Notification;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.exception.CustomException;
import com.homecomingday.exception.ErrorCode;
import com.homecomingday.repository.EmitterRepository;
import com.homecomingday.repository.EmitterRepositoryImpl;
import com.homecomingday.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();

    private final NotificationRepository notificationRepository;


    public SseEmitter subscribe(Long userId, String lastEventId) {
        // 1
        String id = userId + "_" + System.currentTimeMillis();

        // 생성된 emiiterId를 기반으로 emitter를 저장
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));
        //시간 만료후 Repository에서 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("see 연결 오류!");
        }
    }

    //댓글이나 좋아요 누르면 알림
    @Async //비동기 메세지
    public void send(Member receiver, NoticeType noticeType, String message, Long articlesId, String title, LocalDateTime createdAt) {
//        여기 createdAt은 댓글 생성될때 찍히는시간,
        Notification notification = notificationRepository.save(createNotification(receiver, noticeType, message, articlesId, title));
        log.info("DB 메시지 저장 확인 : {}", message);
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


    private Notification createNotification(Member receiver, NoticeType noticeType, String message,
                                            Long articlesId, String title) {

        return Notification.builder()
                .receiver(receiver)
                .noticeType(noticeType)
                .message(message)
                .articlesId(articlesId)
                .title(title)
                .readState(false) // 현재 읽음상태
                .build();
    }

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
    //알림 전체 조회
    @Transactional
    public List<NotificationResponseDto> findAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiver_MemberId(userId);
        return notifications.stream()
                .map(NotificationResponseDto::create)
                .collect(Collectors.toList());
    }

    //알림 전체조회 후 읽음 처리
    @Transactional
    public void readNotification(Long notificationId) {

        //알림을 받은 사람의 id 와 알림의 id 를 받아와서 해당 알림을 찾는다.
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        Notification checkNotification = notification.orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
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
    public String deleteAllByNotification(UserDetailsImpl userDetails) {
        Long receiverId = userDetails.getMember().getId();
        notificationRepository.deleteAllByReceiver_Id(receiverId);
        return "알림 전체 삭제 되었습니다";
    }

    //단일 알림 삭제
    @Transactional
    public String deleteByNotifications(Long notificationId) {
        if (notificationId != null) {
//            throw new CustomException(ErrorCode.NOT_EXIST_NOTIFICATION);
            notificationRepository.deleteById(notificationId);
        }
        return notificationId+" 삭제 되었습니다.";
    }



}
