package com.homecomingday.member.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {
    @NotBlank
    private String address;
    @NotBlank
    private String title;
    @NotBlank
    private String message;
}