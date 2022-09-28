package com.homecomingday.chat.responseDto;

import com.homecomingday.chat.requestDto.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadMessageCount {

    private Long otherMemberId;
    private int unreadCount;
    private String roomId;
    private String type;

    public UnreadMessageCount(ChatMessageDto roomMessage) {
        this.type = "UNREAD";
        this.otherMemberId = roomMessage.getOtherMemberId();
        this.unreadCount = roomMessage.getCount();
        this.roomId = roomMessage.getRoomId();
    }
}
