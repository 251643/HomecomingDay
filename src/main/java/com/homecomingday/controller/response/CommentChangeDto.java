package com.homecomingday.controller.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentChangeDto {


    private Long Id;

    private String msg;

    public CommentChangeDto(Long Id, String msg) {
        this.Id = Id;
        this.msg = msg;
    }
}
