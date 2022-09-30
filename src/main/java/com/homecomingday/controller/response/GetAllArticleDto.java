package com.homecomingday.controller.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllArticleDto {

    private Long articleId;

    private String title;

    @Size(max=400)
    private String content;

    private String calendarDate;

    private String calendarTime;

    private String calendarLocation;

    private String username;

    private Long userId;

    private String email;

    private String createdAt;

    private String admission;

    private String departmentName;

    private String userImage;

    private String articleFlag;

    private Long views;

    private Long heartCnt;

    private Long commentCnt;

    private boolean isHeart;


}