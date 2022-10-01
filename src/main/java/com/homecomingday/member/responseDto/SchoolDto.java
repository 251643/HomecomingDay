package com.homecomingday.member.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SchoolDto {
    private int seq;
    private String schoolName;
    private String address;
}
