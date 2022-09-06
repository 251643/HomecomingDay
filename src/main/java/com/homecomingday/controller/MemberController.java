package com.homecomingday.controller;

import com.homecomingday.controller.request.*;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.service.MemberService;
import com.homecomingday.service.NaverLoginService;
import com.homecomingday.service.NaverUserInfoService;
import com.homecomingday.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  private final NaverLoginService naverLoginService;
  private final NaverUserInfoService naverUserInfoService;
  private final SendEmailService sendEmailService;

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

  @RequestMapping(value = "/member/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }

  //네이버 로그인
  @RequestMapping(value = "/member/callback", method = { RequestMethod.GET, RequestMethod.POST })
  public TokenDto callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, HttpServletResponse response) throws IOException, ParseException {
    System.out.println(code);
    System.out.println(state);
    return naverLoginService.naverLoginCallback(model, code, state, session, response);
  }

  @RequestMapping(value = "/member/naverUserInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
  public TokenDto naverUserInfo(@RequestHeader(value="Authorization") String token, HttpServletResponse response)throws ParseException {

    return naverUserInfoService.naverUserInfo(token, response);
  }

  @PostMapping("/member/signup/sendEmail")
  public @ResponseBody void sendEmail(@RequestBody EmailRequestDto emailRequestDto){
    MailDto dto = sendEmailService.createMail(emailRequestDto);
    sendEmailService.mailSend(dto);
  }
  @PostMapping("/member/signup/checkEmail")
  public ResponseDto<?> checkEmail(@RequestBody EmailRequestDto emailRequestDto){
    return sendEmailService.checkEmail(emailRequestDto);
  }



  //로그아웃
//  @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
//  public String logout(HttpSession session)throws IOException {
//    System.out.println("여기는 logout");
//    session.invalidate();
//    return "로그아웃";
//  }

}
