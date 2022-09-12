package com.homecomingday.controller.response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private Long articleId;

    private String articleFlag;

    private String title;

    private String content;

    private String calendarDate;

    private String calendarTime;

    private String calendarLocation;

    private String username;

    private String createdAt;

    private String admission;

    private String departmentName;

    private Long views;

    private Long heartCnt;

    private List<ImagePostDto> imageList;

    private Long commentCnt;

    private List<CommentResponseDto> commentList;






}
