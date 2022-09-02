package com.homecomingday.controller.response;

import com.homecomingday.controller.request.ImageDto;
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

    private List<CommentDto> commentDtoList;

}
