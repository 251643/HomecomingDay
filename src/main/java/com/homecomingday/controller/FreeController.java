package com.homecomingday.controller;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.FreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequestMapping("/auth/article")
@RestController
@RequiredArgsConstructor
public class FreeController {

    private final FreeService freeService;




    @GetMapping("/free") //자유게시판 메인홈 조회
    public List<ArticleResponseDto> readAllFree(){return freeService.readAllFree();}

    //게시글 작성
    @PostMapping(value = "/free", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> upload(@RequestPart(required = false) ArticleRequestDto articleRequestDto,
                                 @RequestPart (required = false) List<MultipartFile> multipartFile,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                 HttpServletRequest request) throws IOException {
        return freeService.postFree(multipartFile, articleRequestDto,userDetails, request);
    }


    //해당 게시물 상세조회
    @GetMapping("/free/{Id}")
    public ArticleResponseDto readFree(@PathVariable  Long Id,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return freeService.readFree(Id,userDetails);
    }




//    @GetMapping("/{articleId}")
//    public A

}
