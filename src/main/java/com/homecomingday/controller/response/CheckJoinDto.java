package com.homecomingday.controller.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckJoinDto {

    private Long articleId;

    private String email;

    private boolean joinCheck;

    private Integer joinPeople;
}
