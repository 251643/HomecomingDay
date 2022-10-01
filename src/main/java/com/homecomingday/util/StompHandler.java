package com.homecomingday.util;

import com.homecomingday.chat.RedisRepository;
import com.homecomingday.domain.Member;
import com.homecomingday.jwt.JwtDecoder;
import com.homecomingday.member.MemberRepository;
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
        String jwtToken = "";

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // toDo : 모든 화면에서 socket이 뚫려 있기 때문에 대화방에서 온 connect라는 것을 알 수 있는 것이 있어야 한다.

            String type = accessor.getFirstNativeHeader("type");
            if (type !=null && type.equals("CHAT")) {
                // 사용자 확인
                jwtToken = Objects.requireNonNull(accessor.getFirstNativeHeader("token"));

                String username = jwtDecoder.decodeUsername(jwtToken) ;

                Member member = memberRepository.findByEmail(username).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                Long userId = member.getId();
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                redisRepository.saveMyInfo(sessionId, userId);
            }

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) { // Websocket 연결 종료
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String type = accessor.getFirstNativeHeader("type");
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
        return message;
    }
}