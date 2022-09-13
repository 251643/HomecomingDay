package com.homecomingday.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDetailResponseDto {



    private Long articleId;

    private String title;

    private String username;

    private String departmentName;

    private String createdAt;

    private String admission;

    private String articleFlag;

    private Long views;

    private Long heartCnt;

    private Long commentCnt;



}
