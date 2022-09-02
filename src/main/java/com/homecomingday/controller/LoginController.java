package com.homecomingday.controller;

import com.homecomingday.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


/** * Handles requests for the application home page. */

@RestController
@RequiredArgsConstructor
public class LoginController {
    /* NaverLoginBO */
    private final NaverLoginService naverLoginService;


    //로그인 첫 화면 요청 메소드
    @RequestMapping(value = "/naverLogin", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(Model model, HttpSession session) {
        naverLoginService.naverLogin(model, session);

        return "이거 된거 맞냐";
    }


}
