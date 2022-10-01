package com.homecomingday.article.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class joinPeopleDto {

    private String email;

    private String userImage;

    private String username;

    private String department;

    private String admission;

    private boolean joinCheck;

}
