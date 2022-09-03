package com.homecomingday.controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImagePostDto {

    private Long imageId;

    private String imgUrl;
}
