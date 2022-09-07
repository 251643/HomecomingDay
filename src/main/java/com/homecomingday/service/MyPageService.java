package com.homecomingday.service;

import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.MemberResponseDto;
import com.homecomingday.controller.response.MyPageResponseDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.MemberRepository;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class MyPageService {

   // private MyPageRepository mypageRepository;

    //유저 정보 조회


    public MyPageResponseDto readMyPage(UserDetailsImpl member) {
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto();
        MyPageResponseDto.builder()
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .admission(member.getMember().getAdmission())
                .build();
        return myPageResponseDto;
    }


    //# 내가 쓴 게시글 조회
}
