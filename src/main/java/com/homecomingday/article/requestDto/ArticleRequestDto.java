package com.homecomingday.article.requestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleRequestDto {

    private String title;

    private String content;

    private String calendarDate;

    private String calendarTime;

    private String calendarLocation;

    private Integer maxPeople;
}
