package com.homecomingday.chatRoom.responseDto;

import com.homecomingday.domain.Member;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatRoomOtherMemberInfoResponseDto {

    private Long otherUserId;
    private String otherUsername;
    private String otherDepartment;
    private String otherAdmission;

    public ChatRoomOtherMemberInfoResponseDto(Member otherUser) {
        this.otherUserId = otherUser.getId();
        this.otherUsername = otherUser.getUsername();
        this.otherDepartment = otherUser.getDepartmentName();
        this.otherAdmission = otherUser.getAdmission().substring(2, 4) + "학번";
    }
}
