package com.homecomingday.service;


import com.homecomingday.controller.S3Dto;
import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.CommentResponseDto;
import com.homecomingday.controller.response.ImagePostDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.*;
import com.homecomingday.jwt.TokenProvider;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.ImageRepository;
import com.homecomingday.service.s3.S3Uploader;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final S3Uploader s3Uploader;
    private final Time time;
    private final ImageRepository imageRepository;
    private final TokenProvider tokenProvider;

    public List<ArticleResponseDto> readAllFree(String articleFlag) {

        List<Article> articleList = articleRepository.findByArticleFlagOrderByCreatedAtDesc(articleFlag);

        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        for (Article article : articleList) {
            articleResponseDtoList.add(
                    ArticleResponseDto.builder()
                            .articleId(article.getId())
                            .title(article.getTitle())
                            .username(article.getMember().getUsername())
                            .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                            .admission(article.getMember().getAdmission())
                            .views(article.getId())
                            .commentCnt(0L) // 0으로 기본세팅
                            .build()
            );
        }
        return articleResponseDtoList;
    }

    //게시글 생성
    @Transactional
    public ResponseDto<?> postFree(String articleFlag,
                                   List<MultipartFile> multipartFile,
                                   ArticleRequestDto articleRequestDto,
                                   UserDetailsImpl userDetails,
                                   HttpServletRequest request) throws IOException {

        List<Image> imageList = new ArrayList<>();
        if (multipartFile != null) {
            //          이미지 업로드
//            imageList.add(s3Uploader.upload(multipartFile));
            Article article = Article.builder()
                    .title(articleRequestDto.getTitle())
                    .content(articleRequestDto.getContent())
                    .views(0L)
                    .member(userDetails.getMember())
                    .articleFlag(articleFlag)
                    .build();
            articleRepository.save(article);


            for (MultipartFile uploadedFile : multipartFile) {
                S3Dto s3Dto = s3Uploader.upload(uploadedFile);

                Image image = Image.builder()
                        .imgUrl(s3Dto.getUploadImageUrl())
                        .urlPath(s3Dto.getFileName())
                        .article(article)
                        .build();
                imageList.add(image);
                imageRepository.save(image);

            }

//            작성시간 조회

            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .username(article.getMember().getUsername())
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission())
                    .views(article.getId())
                    //.image(imageList)
                    .commentCnt(0L) // 0으로 기본세팅
                    .build();
            return ResponseDto.success(articleResponseDto);

        } else {
            Article article = Article.builder()
                    .title(articleRequestDto.getTitle())
                    .content(articleRequestDto.getContent())
                    .views(0L)
                    .member(userDetails.getMember())
                    .articleFlag(articleFlag)
                    .build();
            articleRepository.save(article);
//            작성시간 조회


            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .username(article.getMember().getUsername())
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission())
                    .views(article.getId())
               //     .image(article.getImageList())
                    .commentCnt(0L) // 0으로 기본세팅
                    .build();
            return ResponseDto.success(articleResponseDto);
        }

    }


    // 게시글 상세 조회
    public ArticleResponseDto readFree(Long articleId, String articleFlag, UserDetailsImpl userDetails) {
        Article article = isPresentArticle(articleId);
        if (null == article) {
            throw new RuntimeException("해당 게시글이 없습니다.");
        }

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<ImagePostDto> imageList = new ArrayList<>();

//        for (Comment comment : article.getComments()) {
//            commentResponseDtoList.add(
//                    CommentResponseDto.builder()
//                            .id(comment.getId())
//                            .nickname(comment.getMember().getNickname())
//                            .content(comment.getContent())
//                            .timeMsg(com.example.intermediate.shared.Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
//                            .build()
//            );
//        }


        for(Image image : article.getImageList()){
            imageList.add(
                    ImagePostDto.builder()
                            .imageId(image.getId())
                            .imgUrl(image.getImgUrl())
                            .build()
            );
        }

        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .username(article.getMember().getUsername())
                .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                .admission(article.getMember().getAdmission())
                .views(article.getViews())
                .image(imageList)
                .commentCnt((long) commentResponseDtoList.size())
                .commentList(commentResponseDtoList)
                .build();
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalPost = articleRepository.findById(id);
        return optionalPost.orElse(null);
    }

}
