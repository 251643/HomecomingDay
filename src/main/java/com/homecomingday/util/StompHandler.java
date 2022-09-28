package com.homecomingday.util;

import com.homecomingday.chat.RedisRepository;
import com.homecomingday.domain.Member;
import com.homecomingday.jwt.JwtDecoder;
import com.homecomingday.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final RedisRepository redisRepository;
    private final JwtDecoder jwtDecoder;
    private final MemberRepository memberRepository;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("accessor>>>>>>>>>      " + accessor);
        String jwtToken = "";

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // toDo : 모든 화면에서 socket이 뚫려 있기 때문에 대화방에서 온 connect라는 것을 알 수 있는 것이 있어야 한다.

            System.out.println("여기는 CONNECT 입니다.");
            String type = accessor.getFirstNativeHeader("type");
            if (type !=null && type.equals("CHAT")) {
                // 사용자 확인
                jwtToken = Objects.requireNonNull(accessor.getFirstNativeHeader("token"));

                String username = jwtDecoder.decodeUsername(jwtToken) ;
                System.out.println("연결된 유저 이메일 : "+username);

                Member member = memberRepository.findByEmail(username).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                Long userId = member.getId();
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                redisRepository.saveMyInfo(sessionId, userId);
            }

            System.out.println("타입이 CHAT이 아니고 " + type + "입니다.");
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) { // Websocket 연결 종료
            System.out.println("여기는 DISCONNECT 입니다.");
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String type = accessor.getFirstNativeHeader("type");
            System.out.println("타입이 CHAT이 아니고 " + type + "입니다.");
            // 채팅방에서 나가는 것이 맞는지 확인 작업
            if (redisRepository.existMyInfo(sessionId)) {
                Long userId = redisRepository.getMyInfo(sessionId);

                // 채팅방 퇴장 정보 저장
                if (redisRepository.existChatRoomUserInfo(userId)) {
                    redisRepository.exitUserEnterRoomId(userId);
                }

                redisRepository.deleteMyInfo(sessionId);
            }
        }
        System.out.println("message는 : " + message);
        return message;
    }
}