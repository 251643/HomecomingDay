package com.homecomingday.controller.response;

import com.homecomingday.domain.Comment;
import com.homecomingday.domain.NoticeType;
import com.homecomingday.domain.Notification;
import com.homecomingday.util.ArticleChange;
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

    private String noticeType;

    private String articleFlag;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String message,String username, Long articleId, Boolean readStatus,
                                   String noticeType, String title, String createdAt, String articleFlag) {
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
                .noticeType(ArticleChange.changeNoticeType(String.valueOf(notification.getNoticeType())))
                .articleId(notification.getUrl())
                .title(notification.getTitle())
                .username(notification.getComment().getMember().getUsername())
                .articleFlag(ArticleChange.changearticleFlag(notification.getComment().getArticleFlag()))
                .readStatus(notification.getReadState())
                .createdAt(createdAt)
                .build();
    }
}