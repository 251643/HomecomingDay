package com.homecomingday.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SchoolDto {
    private int seq;
    private String schoolName;
    private String address;
}
