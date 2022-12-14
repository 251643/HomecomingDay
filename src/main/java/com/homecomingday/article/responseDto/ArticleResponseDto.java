package com.homecomingday.article.responseDto;



import com.homecomingday.comment.responseDto.CommentResponseDto;
import com.homecomingday.article.requestDto.ImagePostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto implements Serializable {

    private Long articleId;

    private String articleFlag;

    private String title;

    private String content;

    private String calendarDate;

    private String calendarTime;

    private String calendarLocation;

    private Integer maxPeople;

    private String username;

    private Long userId;

    private String email;

    private String userImage;

    private String createdAt;

    private String admission;

    private String departmentName;

    private Long views;

    private Long heartCnt;

    private List<ImagePostDto> imageList;

    private Long commentCnt;

    private List<CommentResponseDto> commentList;

    private boolean isHeart;





}
