package com.homecomingday.article.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmHeartDto {

    private Long heartCnt;
    private Boolean isHeart;
}