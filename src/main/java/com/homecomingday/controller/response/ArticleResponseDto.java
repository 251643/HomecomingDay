package com.homecomingday.controller.response;


import com.homecomingday.domain.Comment;
import com.homecomingday.domain.Image;
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
//
    private List<ImagePostDto>imageList;

    private Long commentCnt;

    private List<CommentResponseDto> commentList;


    //전체조회시 출력값
//    public ArticleResponseDto(Article article, String createdAt){
//        this.articleId= article.getId();
//        this.title= article.getTitle();
//        this.username=getUsername();
//        this.createdAt=createdAt; // 몇분전 부터 출력
//        this.admission=getAdmission();
//        this.views=getViews();
   //     this.commentCnt= (long) article.getComments().size(); // 처음 작성했을시에는 댓글이 0인데 사이즈를 어떻게 가지고 올 것 인지?

  //  }

//    //상세페이지 출력값
//    public ArticleResponseDto(Free free,List<String> image, String createdAt, List<CommentDto> commentDtoList) {
//        this.articleId = free.getId();
//        this.username = free.getMember().getUsername();
//        this.content = free.getContent();
//        this.createdAt = createdAt;
//        this.commentList = commentDtoList;
//        this.image = image;
//    }

}
