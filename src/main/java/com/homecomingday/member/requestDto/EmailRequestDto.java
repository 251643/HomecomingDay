package com.homecomingday.member.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;


public class EmailRequestDto {

    @Getter
    @NoArgsConstructor
    public static class EmailSendRequestDto{ //static? 바로가지고올수있다?
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class AuthRequestDto{ //static? 바로가지고올수있다?
        private String email;
        private String authKey;
    }
}
