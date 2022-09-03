package com.homecomingday.controller;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;




    @GetMapping("/article/{articleFlag}") //자유게시판 메인홈 조회
    public List<ArticleResponseDto> readAllFree(@PathVariable String articleFlag){
        return articleService.readAllFree(articleFlag);
    }

    //게시글 작성
    @PostMapping(value = "/article/{articleFlag}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> upload(@PathVariable String articleFlag,
                                 @RequestPart(required = false, value="articleRequestDto") ArticleRequestDto articleRequestDto,
                                 @RequestPart (required = false, value="files") List<MultipartFile> multipartFile,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                 HttpServletRequest request) throws IOException {
        return articleService.postFree(articleFlag, multipartFile, articleRequestDto,userDetails, request);
    }


    //해당 게시물 상세조회
    @GetMapping("/article/{articleFlag}/{Id}")
    public ArticleResponseDto readFree(@PathVariable Long Id, @PathVariable String articleFlag, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.readFree(Id, articleFlag,userDetails);
    }




//    @GetMapping("/{articleId}")
//    public A

}
