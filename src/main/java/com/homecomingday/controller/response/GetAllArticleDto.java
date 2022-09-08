package com.homecomingday.controller.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllArticleDto {



    private Long articleId;

    private String title;

    private String username;

    private String createdAt;

    private String admission;

    private String articleFlag;

    private Long views;

    private Long commentCnt;



}