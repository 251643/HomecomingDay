package com.homecomingday.myPage.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {

    private String schoolName;

    private Long userId;

    private String email;

    private String username;

    private String userImage;

    private String departmentName;

    private String admission;

    private int articleCnt;
}
