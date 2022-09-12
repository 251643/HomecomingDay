package com.homecomingday.controller;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

     //내가 쓴 게시글 조회
    @GetMapping("/myPage/myArticle")
    public List<MyPageDetailResponseDto> readDetailMyPage(@AuthenticationPrincipal UserDetailsImpl member){
        return myPageService.readDetailMyPage(member);
    }


}
