package com.homecomingday.chat;

import com.amazonaws.services.kms.model.NotFoundException;
import com.homecomingday.chat.requestDto.ChatMessageDto;
import com.homecomingday.domain.Member;
import com.homecomingday.jwt.JwtDecoder;
import com.homecomingday.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;
    private final JwtDecoder jwtDecoder;
    private final MemberRepository memberRepository;

    /**
     * websocket "/pub/chat/enter"로 들어오는 메시징을 처리한다.
     * 채팅방에 입장했을 경우
     */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessageDto, @Header("token") String token) {
        String email = jwtDecoder.decodeUsername(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        chatService.enter(member.getId(), chatMessageDto.getRoomId());
    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto chatMessageDto, @Header("token") String token) {
        String username = jwtDecoder.decodeUsername(token);
        Member member = memberRepository.findByEmail(username).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        chatService.sendMessage(chatMessageDto, member);
        chatService.updateUnReadMessageCount(chatMessageDto);
    }

}