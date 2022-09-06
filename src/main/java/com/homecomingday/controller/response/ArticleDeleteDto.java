package com.homecomingday.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDeleteDto {

    private Long Id;

    private String msg;

    public ArticleDeleteDto(Long Id, String msg) {
        this.Id = Id;
        this.msg = msg;
    }
}
