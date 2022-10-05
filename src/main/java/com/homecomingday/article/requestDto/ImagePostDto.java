package com.homecomingday.article.requestDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImagePostDto implements Serializable {

    private Long imageId;

    private String imgUrl;
}
