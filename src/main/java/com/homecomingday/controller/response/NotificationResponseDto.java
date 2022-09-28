package com.homecomingday.controller.response;

import com.homecomingday.domain.Comment;
import com.homecomingday.domain.NoticeType;
import com.homecomingday.domain.Notification;
import com.homecomingday.util.Time;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long notificationId;
    private Long articleId;

    private String message;

    private String username;

    private Boolean readStatus;

    private NoticeType noticeType;

    private String articleFlag;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String message,String username, Long articleId, Boolean readStatus,
                                   NoticeType noticeType, String title, String createdAt, String articleFlag) {
        this.notificationId = id;
        this.message = message;
        this.username = username;
        this.articleId = articleId;
        this.readStatus = readStatus;
        this.articleFlag = articleFlag;
        this.title = title;
        this.noticeType = noticeType;
        this.createdAt = createdAt;
    }

    public static NotificationResponseDto create(Notification notification) {
        long now = ChronoUnit.MINUTES.between(notification.getCreatedAt() , LocalDateTime.now());
        Time time = new Time();
        String createdAt = time.times(now);

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .noticeType(notification.getNoticeType())
                .articleId(notification.getUrl())
                .title(notification.getTitle())
                .username(notification.getComment().getMember().getUsername())
                .articleFlag(notification.getComment().getArticleFlag())
                .readStatus(notification.getReadState())
                .createdAt(createdAt)
                .build();
    }
//
//    public static NotificationResponseDto create(Notification notification,Comment comment) {
//        long now = ChronoUnit.MINUTES.between(notification.getCreatedAt() , LocalDateTime.now());
//        Time time = new Time();
//        String createdAt = time.times(now);
//
//        return NotificationResponseDto.builder()
//                .id(notification.getId())
//                .message(notification.getMessage())
//                .noticeType(notification.getNoticeType())
//                .username(notification.getMember().getUsername())
//                .articleId(notification.getUrl())
//                .title(notification.getTitle())
//                .readStatus(notification.getReadState())
//                .createdAt(createdAt)
//                .build();
//    }
}