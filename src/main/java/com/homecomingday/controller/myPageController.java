package com.homecomingday.controller;

import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.controller.response.MyPageResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class myPageController {

    private final MyPageService myPageService;

    //유저 정보 조회
    @GetMapping("/myPage")
    public MyPageResponseDto readMyPage( @AuthenticationPrincipal UserDetailsImpl member){
        return myPageService.readMyPage(member);

    }

    // 내가 쓴 게시글 조회
    @GetMapping("/myPage/myArticle")
    public List<MyPageDetailResponseDto> readDetailMyPage(@AuthenticationPrincipal UserDetailsImpl member){
        return myPageService.readDetailMyPage(member);
    }


}
