package com.homecomingday.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.homecomingday.controller.response.NaverMemberInfoDto;
import com.homecomingday.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
