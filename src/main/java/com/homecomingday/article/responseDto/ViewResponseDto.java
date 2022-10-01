package com.homecomingday.article.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewResponseDto {
    private Long articleId;

    private Long views;
}
