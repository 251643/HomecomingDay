package com.homecomingday.controller;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.*;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;


    //게시글 메인홈 조회
    @GetMapping("/article/{articleFlag}")
    public List<GetAllArticleDto> readAllArticle(@PathVariable String articleFlag){

        return articleService.readAllArticle(articleFlag);
    }

    //게시글 작성
    @PostMapping(value = "/article/{articleFlag}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ArticleResponseDto upload(@PathVariable String articleFlag,
                                 @RequestPart(required = false, value="articleRequestDto") ArticleRequestDto articleRequestDto,
                                 @RequestPart (required = false,value="files") List<MultipartFile> multipartFile,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     MultipartHttpServletRequest  request) throws IOException {
        return articleService.postArticle(articleFlag, multipartFile, articleRequestDto,userDetails, request);
    }


    //해당 게시물 상세조회
    @GetMapping("/article/{articleFlag}/{articleId}")
    public ArticleResponseDto readArticle( @PathVariable String articleFlag,@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.readArticle(articleFlag,articleId,userDetails);
    }


    //게시글 수정
    @PutMapping(value="/article/{articleFlag}/{articleId}")
    public String updateArticle(@PathVariable String articleFlag,@PathVariable Long articleId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody ArticleRequestDto articleRequestDto) {
//        ArticleResponseDto articleResponseDto=articleService.updateArticle(Id,articleFlag,userDetails,articleRequestDto);

        return articleService.updateArticles(articleFlag,articleId,userDetails,articleRequestDto);
    }

    //게시글 삭제
    @DeleteMapping("/article/{articleFlag}/{articleId}")
    public String deleteArticle(@PathVariable String articleFlag,@PathVariable Long articleId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.deleteArticles(articleFlag,articleId,userDetails);
    }


}
