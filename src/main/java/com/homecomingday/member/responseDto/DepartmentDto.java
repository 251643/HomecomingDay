package com.homecomingday.member.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DepartmentDto {
    private int seq;
    private String mClass;
}