package com.homecomingday.article.responseDto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckJoinDto {

    private String email;

    private String userImage;

    private String username;

    private String department;

    private String admission;

    private boolean joinCheck;

    private Integer joinPeople;
}
