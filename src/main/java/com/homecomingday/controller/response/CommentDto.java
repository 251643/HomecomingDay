package com.homecomingday.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {



    private Long commentId;

    private String content; // 댓글내용

    private String username;

    private String createdAt;




//    public CommentDto(FreeComment freeComment, String username, String createAt ){
//        this.articleId=FreeComment.getFree
//    }
}
