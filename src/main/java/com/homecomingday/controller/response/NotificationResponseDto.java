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

import static com.homecomingday.domain.NoticeType.comment;
import static com.homecomingday.domain.NoticeType.heart;

@NoArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long notificationId;
    private Long articleId;

    private String username;

    private Boolean readStatus;

    private String noticeType;

    private String articleFlag;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String username, Long articleId, Boolean readStatus,
                                   String noticeType, String title, String createdAt, String articleFlag) {
        this.notificationId = id;
        this.username = username;
        this.articleId = articleId;
        this.readStatus = readStatus;
        this.articleFlag = articleFlag;
        this.title = title;
        this.noticeType = noticeType;
        this.createdAt = createdAt;
    }

    public static NotificationResponseDto create(Notification notification) {
        long now = ChronoUnit.MINUTES.between(notification.getCreatedAt(), LocalDateTime.now());
        Time time = new Time();
        String createdAt = time.times(now);
        if (notification.getNoticeType() == comment) {
            return NotificationResponseDto.builder()
                    .id(notification.getId())
                    .noticeType(ArticleChange.changeNoticeType(String.valueOf(notification.getNoticeType())))
                    .articleId(notification.getUrl())
                    .title(notification.getTitle())
                    .username(notification.getComment().getMember().getUsername())
                    .articleFlag(ArticleChange.changearticleFlag(notification.getComment().getArticleFlag()))
                    .readStatus(notification.getReadState())
                    .createdAt(createdAt)
                    .build();
        } else if (notification.getNoticeType() == heart) {
            return NotificationResponseDto.builder()
                    .id(notification.getId())
                    .noticeType(ArticleChange.changeNoticeType(String.valueOf(notification.getNoticeType())))
                    .articleId(notification.getUrl())
                    .title(notification.getTitle())
                    .username(notification.getHeart().getMember().getUsername())
                    .articleFlag(ArticleChange.changearticleFlag(notification.getHeart().getArticle().getArticleFlag()))
                    .readStatus(notification.getReadState())
                    .createdAt(createdAt)
                    .build();


        }
        return null;}
}