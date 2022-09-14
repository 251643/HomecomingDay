package com.homecomingday.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitResponseDto {


    private Long childCommentId;

    private String content;

    private String username;

    private String userImage;

    private String admission;

    private String departmentName;

    private String  createdAt;

}
