package com.homecomingday.comment.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitResponseDto implements Serializable {


    private Long childCommentId;

    private String content;

    private String username;

    private Long userId;

    private String email;

    private String userImage;

    private String admission;

    private String departmentName;

    private String  createdAt;

}
