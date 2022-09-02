package com.homecomingday.controller.response;


import com.homecomingday.domain.Free;
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

    private String title;

    private String content;

    private String username;

    private String createdAt;

    private String admission;

    private Long views;

//    private Long commentCnt; 어떻게 가져가야하는지 모르겠음

    private List<String>image;

    private Long commentCnt;

    private List<CommentDto> commentList;


    //전체조회시 출력값
    public ArticleResponseDto(Free free,String createdAt){
        this.articleId=free.getId();
        this.title=free.getTitle();
        this.username=getUsername();
        this.createdAt=createdAt;
        this.admission=getAdmission();
        this.views=getViews();
        this.commentCnt= (long) free.getFreeComments().size();

    }

    //상세페이지 출력값
    public ArticleResponseDto(Free free,List<String> image, String createdAt, List<CommentDto> commentDtoList) {
        this.articleId = free.getId();
        this.username = free.getMember().getUsername();
        this.content = free.getContent();
        this.createdAt = createdAt;
        this.commentList = commentDtoList;
        this.image = image;
    }

}
