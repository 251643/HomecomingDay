package com.homecomingday.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;


public class EmailRequestDto {

    @Getter
    @NoArgsConstructor
    public static class EmailSendRequestDto{ //static? 바로가지고올수있다?
        private String sendEmail;
    }

    @Getter
    @NoArgsConstructor
    public static class AuthRequestDto{ //static? 바로가지고올수있다?
        private String authEmail;
        private String authKey;
    }
}
