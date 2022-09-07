package com.homecomingday.controller.response;

import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.domain.Article;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.util.Time;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Article2ResponseDto {
    private Long articleId;

    private String articleFlag;

    private String title;

    private String content;

    private String username;

    private String createdAt;

    private String admission;

    private Long views;

    private List<ImagePostDto> imageList;

    private Long commentCnt;

    private List<CommentResponseDto> commentList;

    public Article2ResponseDto(Article article,String articleFlag,String admission,List<ImagePostDto> imageList){
        this.articleId=article.getId();
        this.articleFlag=articleFlag;
        this.title=article.getTitle();
        this.content=article.getContent();
        this.username=article.getMember().getUsername();
        this.createdAt=Time.convertLocaldatetimeToTime(article.getCreatedAt());
        this.admission=admission;
        this.views=0L;
        this.imageList=imageList;
        this.commentCnt=0L;
    }

    public Article2ResponseDto(Article article,String articleFlag,String admission){
        this.articleId=article.getId();
        this.articleFlag=articleFlag;
        this.title=article.getTitle();
        this.content=article.getContent();
        this.username=article.getMember().getUsername();
        this.createdAt= Time.convertLocaldatetimeToTime(article.getCreatedAt());
        this.admission=admission;
        this.views=0L;
        this.imageList=null;
        this.commentCnt=0L;
    }

}
