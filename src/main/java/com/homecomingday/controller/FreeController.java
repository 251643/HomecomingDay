package com.homecomingday.controller;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.domain.Free;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.FreeService;
import com.homecomingday.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/article/free")
@RestController
@RequiredArgsConstructor
public class FreeController {

    private final FreeService freeService;




    @GetMapping("") //자유게시판 메인홈 조회
    public List<ArticleResponseDto> readAllFree(){return freeService.readAllFree();}

    //게시글 작성
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ArticleResponseDto upload(@RequestPart(required = false) ArticleRequestDto articleRequestDto,
                                     @RequestPart (required = false) List<MultipartFile> multipartFile,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return freeService.postFree(multipartFile, articleRequestDto,userDetails);
    }


    //해당 게시물 상세조회
    @GetMapping("/{Id}")
    public ArticleResponseDto readFree(@PathVariable  Long Id,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return freeService.readFree(Id,userDetails);
    }




//    @GetMapping("/{articleId}")
//    public A

}
