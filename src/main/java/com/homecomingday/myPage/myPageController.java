package com.homecomingday.myPage;

import com.homecomingday.myPage.responseDto.MyPageDetailResponseDto;
import com.homecomingday.myPage.responseDto.MyPageResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class myPageController {

    private final MyPageService myPageService;
    private final ArticleRepository articleRepository;

    @GetMapping("/myPage/myArticle2")
    public ResponseEntity<Page<MyPageDetailResponseDto>> getArticleScroll(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(articleRepository.getArticleScroll2(pageable, userDetails));
    }

    @GetMapping("/myPage/reset")
    public String reset(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userDetails.getUsername() + "으로 요청들어옴";
    }

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

//    // 내가 쓴 게시글 조회
//    @GetMapping("/myPage/myArticle")
//    public List<MyPageDetailResponseDto> readDetailMyPage(@AuthenticationPrincipal UserDetailsImpl member){
//        return myPageService.readDetailMyPage(member);
//    }
//


}
