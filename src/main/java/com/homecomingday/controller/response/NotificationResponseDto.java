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

    private String username;

    private Boolean readStatus;

    private NoticeType noticeType;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, Comment comment, Long articleId, Boolean readStatus,
                                   NoticeType noticeType, String title, String createdAt) {
        this.notificationId = id;
        this.username = comment.getMember().getUsername();
        this.articleId = articleId;
        this.readStatus = readStatus;
        this.title = title;
        this.noticeType = noticeType;
        this.createdAt = createdAt;
    }

    public static NotificationResponseDto create(Notification notification) {
        String now = String.valueOf(ChronoUnit.MINUTES.between(notification.getCreatedAt() , LocalDateTime.now()));
        String createdAt = Time.convertLocaldatetimeToTime(LocalDateTime.parse(now));

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .noticeType(notification.getNoticeType())
                .articleId(notification.getUrl())
                .title(notification.getTitle())
                .readStatus(notification.getReadState())
                .createdAt(createdAt)
                .build();
    }
}