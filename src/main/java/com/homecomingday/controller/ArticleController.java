package com.homecomingday.controller;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleDeleteDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.service.ArticleService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @GetMapping("/api/auth/article")
    public Slice<ArticleResponseDto> getArticleScroll(HttpServletResponse response,
                                                      @PageableDefault(size = 3, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        String nickname = userDetails.getUsername();
        response.setHeader("nickname", nickname);
        return articleRepository.getArticleScroll(pageable, nickname);
    }


    //게시글 메인홈 조회
    @GetMapping("/article/{articleFlag}")
    public List<ArticleResponseDto> readAllArticle(@PathVariable String articleFlag){
        return articleService.readAllArticle(articleFlag);
    }

    //게시글 작성
    @PostMapping(value = "/article/{articleFlag}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> upload(@PathVariable String articleFlag,
                                 @RequestPart(required = false, value="articleRequestDto") ArticleRequestDto articleRequestDto,
                                 @RequestPart (required = false, value="files") List<MultipartFile> multipartFile,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                 HttpServletRequest request) throws IOException {
        return articleService.postArticle(articleFlag, multipartFile, articleRequestDto,userDetails, request);
    }


    //해당 게시물 상세조회
    @GetMapping("/article/{articleFlag}/{Id}")
    public ArticleResponseDto readArticle(@PathVariable Long Id, @PathVariable String articleFlag, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.readArticle(Id, articleFlag,userDetails);
    }


    //게시글 수정
    @PutMapping(value="/article/{articleFlag}/{Id}")
    public ResponseDto<?>  updateArticle(@PathVariable Long Id, @PathVariable String articleFlag,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestBody ArticleRequestDto articleRequestDto) {
        ArticleResponseDto articleResponseDto=articleService.updateArticle(Id,articleFlag,userDetails,articleRequestDto);

        return ResponseDto.success(articleResponseDto);
    }

    //게시글 삭제
    @DeleteMapping("/article/{Id}")
    public ArticleDeleteDto deleteArticle(@PathVariable Long Id,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.deleteArticle(Id,userDetails);
    }


//    @GetMapping("/{articleId}")
//    public A

}
