package com.homecomingday.controller.response;


import com.homecomingday.domain.Free;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private Long articleId;

    private String title;

    private String username;

    private String createdAt;

    private String admission;

    private Long views;

//    private Long commentCnt; 어떻게 가져가야하는지 모르겠음


    public ArticleResponseDto(Free free,String createdAt){
        this.articleId=free.getId();
        this.title=free.getTitle();
        this.username=getUsername();
        this.createdAt=createdAt;
        this.admission=getAdmission();
        this.views=getViews();
//        this.commentCnt

    }

}
