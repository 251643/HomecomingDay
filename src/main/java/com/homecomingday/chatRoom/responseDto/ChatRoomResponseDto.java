package com.homecomingday.chatRoom.responseDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import com.homecomingday.util.Time;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomResponseDto implements Comparable<ChatRoomResponseDto> {
    private String roomName;
    private String chatRoomUuid;
    private String lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    private String dayBefore;
    private int unreadCount;
    private String admission;
    private String departmentName;

    private String otherUserImage;
    private int totalCnt;

    public ChatRoomResponseDto(String roomName, String roomId, String lastMessage,
        LocalDateTime lastTime, int unReadMessageCount, String dayBefore, String admission, String departmentName, String otherUserImage, int totalCnt) {
        this.roomName = roomName;
        this.chatRoomUuid=roomId;
        this.lastMessage=lastMessage;
        this.createAt=lastTime;
        this.dayBefore = dayBefore;
        this.unreadCount = unReadMessageCount;
        this.admission = admission.substring(2, 4) + "학번";
        this.departmentName = departmentName;
        this.otherUserImage = otherUserImage;
        this.totalCnt = totalCnt;
    }

    @Override
    public int compareTo(ChatRoomResponseDto responseDto) {
        if (responseDto.createAt.isBefore(createAt)) {
            return 1;
        } else if (responseDto.createAt.isAfter(createAt)) {
            return -1;
        }
        return 0;
    }
}
