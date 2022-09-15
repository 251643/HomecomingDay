package com.homecomingday.controller;

import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.controller.response.MyPageResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class myPageController {

    private final MyPageService myPageService;
    private final ArticleRepository articleRepository;
//    @GetMapping("/myPage/myArticle")
//    public Slice<MyPageDetailResponseDto> getArticleScroll(
//                                                      @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable
//                                                      ) {
//        return articleRepository.getArticleScroll(pageable);
//    }


    //유저 정보 조회
    @GetMapping("/myPage")
    public MyPageResponseDto readMyPage( @AuthenticationPrincipal UserDetailsImpl member){
        return myPageService.readMyPage(member);

    }

    //유저 정보 이미지 생성
    @PatchMapping(value = "/myPage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MyPageResponseDto updateMyPage(@AuthenticationPrincipal UserDetailsImpl member,@RequestPart (required = false,value="userImage") MultipartFile multipartFile
                                     ) throws IOException {
        return myPageService.updateMyPage(member,multipartFile);
    }

    // 내가 쓴 게시글 조회
    @GetMapping("/myPage/myArticle")
    public List<MyPageDetailResponseDto> readDetailMyPage(@AuthenticationPrincipal UserDetailsImpl member){
        return myPageService.readDetailMyPage(member);
    }


}
