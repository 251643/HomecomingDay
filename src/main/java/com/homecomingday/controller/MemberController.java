package com.homecomingday.controller;

import com.homecomingday.controller.request.*;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.MemberService;
import com.homecomingday.service.NaverLoginService;
import com.homecomingday.service.NaverUserInfoService;
import com.homecomingday.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;
  private final NaverLoginService naverLoginService;
  private final NaverUserInfoService naverUserInfoService;
  private final SendEmailService sendEmailService;

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }
  @RequestMapping(value = "/emailCheck", method = RequestMethod.POST)
  public ResponseDto<?> emailCheck(@RequestBody @Valid EmailRequestDto.EmailSendRequestDto emailSendRequestDto) {
    return memberService.checkEmail(emailSendRequestDto);
  }
  @RequestMapping(value = "/login", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
      HttpServletResponse response ) {
    return memberService.login(requestDto, response);
  }

  @RequestMapping(value = "/schoolInfos", method = RequestMethod.POST)
  public ResponseDto<?> schoolInfo(@RequestBody @Valid SchoolInfoDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return memberService.schoolInfoMember(requestDto, userDetails);
  }

//  @RequestMapping(value = "/reissue", method = RequestMethod.POST)
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    return memberService.reissue(request, response);
//  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }

  @RequestMapping(value = "/naverUserInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
  public TokenDto naverUserInfo(@RequestHeader(value="Authorization", required = false) String token, HttpServletResponse response)throws ParseException {

    return naverUserInfoService.naverUserInfo(token,  response);
  }

  @PostMapping("/signup/sendEmail")
  public @ResponseBody void sendEmail(@RequestBody EmailRequestDto.EmailSendRequestDto emailSendRequestDto){
    MailDto dto = sendEmailService.createMail(emailSendRequestDto);
    sendEmailService.mailSend(dto);
  }

  @PostMapping("/signup/checkEmail")
  public ResponseDto<?> checkEmail(@RequestBody EmailRequestDto.AuthRequestDto authRequestDto){
    return sendEmailService.checkEmail(authRequestDto);
  }



  //로그아웃
//  @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
//  public String logout(HttpSession session)throws IOException {
//    System.out.println("여기는 logout");
//    session.invalidate();
//    return "로그아웃";
//  }
//  //네이버 로그인
//  @RequestMapping(value = "/callback", method = { RequestMethod.GET, RequestMethod.POST })
//  public TokenDto callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, HttpServletResponse response) throws IOException, ParseException {
//    System.out.println(code);
//    System.out.println(state);
//    return naverLoginService.naverLoginCallback(model, code, state, session, response);
//  }

}
