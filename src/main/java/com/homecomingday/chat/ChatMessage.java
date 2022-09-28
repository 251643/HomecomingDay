package com.homecomingday.chat;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.homecomingday.chat.requestDto.ChatMessageDto;
import com.homecomingday.chatRoom.ChatRoom;
import com.homecomingday.domain.Member;
import com.homecomingday.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Entity
@NoArgsConstructor
public class ChatMessage extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 메세지 작성자
    @ManyToOne
    private Member member;

    // 채팅 메세지 내용
    @Size(max = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatMessage(Member member, ChatMessageDto chatMessageDto, ChatRoom chatRoom) {
        this.member = member;
        this.message = chatMessageDto.getMessage();
        this.chatRoom = chatRoom;
    }
}
