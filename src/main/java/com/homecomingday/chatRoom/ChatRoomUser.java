package com.homecomingday.chatRoom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.homecomingday.domain.Member;
import com.homecomingday.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoomUser extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 채팅방 주인
    @ManyToOne
    private Member member;

    // 채팅방 이름
    private String name;
    private String otherUserImage;
    @ManyToOne
    private Member otherMember;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;


    public ChatRoomUser(Member member, Member anotherUser, ChatRoom room) {

        this.member = member;
        this.name = anotherUser.getUsername();
        this.chatRoom = room;
        this.otherMember = anotherUser;
        this.otherUserImage = anotherUser.getUserImage();
    }


}
