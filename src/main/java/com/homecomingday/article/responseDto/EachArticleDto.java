package com.homecomingday.article.responseDto;

import com.homecomingday.article.requestDto.ImageDto;
import com.homecomingday.comment.responseDto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EachArticleDto {

    private Long articleId;

    private String title;

    private String username;

    private String content;

    private List<ImageDto> imageList ;

    private String createdAt;

    private List<CommentResponseDto> commentResponseDtoList;

}
