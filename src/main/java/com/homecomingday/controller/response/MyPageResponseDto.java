package com.homecomingday.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {

    private String email;

    private String username;

    private String departmentName;

    private String admission;
}
