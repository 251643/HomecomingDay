package com.homecomingday.controller.response;


import com.homecomingday.domain.Free;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private Long articleId;

    private String title;

    private String content;

    private String username;

    private String createdAt;

    private String admission;

    private Long views;

//    private Long commentCnt; 어떻게 가져가야하는지 모르겠음

    private List<String>image;


    public ArticleResponseDto(Free free,String createdAt){
        this.articleId=free.getId();
        this.title=free.getTitle();
        this.content=free.getContent();
        this.username=getUsername();
        this.createdAt=createdAt;
        this.admission=getAdmission();
        this.views=getViews();
//        this.commentCnt

    }

    public ArticleResponseDto(Free free,List<String> image, String createdAt, List<CommentDto> commentDtoList) {
        this.articleId = free.getId();
//        this.images = articles.getImage();
        this.username = free.getMember().getUsername();
        this.content = free.getContent();
        this.createdAt = createdAt;
        this.commentList = commentDtoList;
        this.image = image;
    }

}
