package com.homecomingday.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolInfoDto {

    @NotBlank
    private String email;

    @NotBlank
    private String schoolName;

    @NotBlank
    private String departmentName;

    @NotBlank
    private String admission;
}
