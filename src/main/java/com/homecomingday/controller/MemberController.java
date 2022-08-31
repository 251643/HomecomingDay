package com.homecomingday.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homecomingday.controller.request.LoginRequestDto;
import com.homecomingday.controller.request.MemberRequestDto;
import com.homecomingday.controller.request.SchoolInfoDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.homecomingday.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  private final NaverLoginService naverLoginService;

  @RequestMapping(value = "/member/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }

  @RequestMapping(value = "/member/signup/schoolInfo", method = RequestMethod.POST)
  public ResponseDto<?> schoolInfo(@RequestBody @Valid SchoolInfoDto requestDto) {
    return memberService.schoolInfoMember(requestDto);
  }

  @RequestMapping(value = "/member/login", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

//  @RequestMapping(value = "/api/auth/member/reissue", method = RequestMethod.POST)
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    return memberService.reissue(request, response);
//  }

  @RequestMapping(value = "/auth/member/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }

  //네이버 로그인
  @RequestMapping(value = "/member/callback", method = { RequestMethod.GET, RequestMethod.POST })
  public TokenDto callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, HttpServletResponse response) throws IOException, ParseException {

    return naverLoginService.naverLoginCallback(model, code, state, session, response);
  }
  //로그아웃
//  @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
//  public String logout(HttpSession session)throws IOException {
//    System.out.println("여기는 logout");
//    session.invalidate();
//    return "로그아웃";
//  }

}
