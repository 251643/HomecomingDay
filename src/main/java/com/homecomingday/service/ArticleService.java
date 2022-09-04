package com.homecomingday.service;


import com.homecomingday.controller.S3Dto;
import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.*;
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

    private final ImageRepository imageRepository;
    private final TokenProvider tokenProvider;



    //현재 게시물 받아오기
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalPost = articleRepository.findById(id);
        return optionalPost.orElse(null);
    }



    //메인페이지 게시물 조회
    public List<ArticleResponseDto> readAllArticle(String articleFlag) {

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
                            .views(article.getViews())
                            .commentCnt(0L) // 0으로 기본세팅
                            .build()
            );
        }
        return articleResponseDtoList;
    }



    //게시글 만남일정이랑 다른친구 구분값으로 api주던가 flag통해서 구분값주기
    //게시글 생성
    @Transactional
    public ResponseDto<?> postArticle(String articleFlag,
                                      List<MultipartFile> multipartFile,
                                      ArticleRequestDto articleRequestDto,
                                      UserDetailsImpl userDetails,
                                      HttpServletRequest request) throws IOException {

        //게시글
        Article article = Article.builder()
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .views(0L)
                .member(userDetails.getMember())
                .articleFlag(articleFlag)
                .build();
        articleRepository.save(article);
//            작성시간 조회




        if (multipartFile != null) {

            List<Image> imageList = new ArrayList<>();
            //이미지 업로드
            for (MultipartFile uploadedFile : multipartFile) {
                S3Dto s3Dto = s3Uploader.upload(uploadedFile);

                Image image = Image.builder()
                        .imgUrl(s3Dto.getUploadImageUrl())
                        .urlPath(s3Dto.getFileName())
                        .article(article)
                        .build();

                List<ImagePostDto> imagePostDto = new ArrayList<>();
                ImagePostDto.builder()
                        .imageId(image.getId())
                        .imgUrl(image.getImgUrl())
                        .build();

                imageList.add(image);
                imageRepository.save(image);

            }
        }

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .username(article.getMember().getUsername())
                .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                .admission(article.getMember().getAdmission())
                .views(article.getViews())
                //     .image(article.getImageList())
                .commentCnt(0L) // 0으로 기본세팅
                .build();
            return ResponseDto.success(articleResponseDto);
        }




    // 게시글 상세 조회
    public ArticleResponseDto readArticle(Long articleId, String articleFlag, UserDetailsImpl userDetails) {
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


        for (Image image : article.getImageList()) {
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
                .imageList(imageList)
                .commentCnt((long) commentResponseDtoList.size())
                .commentList(commentResponseDtoList)
                .build();
    }


    //게시글 수정 사진수정은 안하기로함
    @Transactional
    public ArticleResponseDto updateArticle(Long articleId, String articleFlag,
                                            UserDetailsImpl userDetails,
                                            ArticleRequestDto articleRequestDto) {
        Article article = isPresentArticle(articleId);
        String email = userDetails.getUsername();  //받는 값은 username으로 되어있으나 email임


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
        for (Image image : article.getImageList()) {
            imageList.add(
                    ImagePostDto.builder()
                            .imageId(image.getId())
                            .imgUrl(image.getImgUrl())
                            .build()
            );
        }


        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .articleId(article.getId())
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .username(userDetails.getMember().getUsername())
                .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                .admission(article.getMember().getAdmission())
                .views(article.getViews())
                .commentCnt((long) commentResponseDtoList.size())
                .build();

        if (email.equals(article.getMember().getEmail())) { //유니크 처리를 email만 해줬기에 기존 작성자와 현로그인한 유저의 이메일을 비교하여 바꿔준다

            article.updateArticle(articleRequestDto);
//            article.updateArticle(articleRequestDto);
            return articleResponseDto;
        }
        return null;
    }


    public ArticleDeleteDto deleteArticle(Long Id,UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(Id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));

        Article articles = isPresentArticle(Id);
        String email=userDetails.getUsername();

        ArticleDeleteDto articleDeleteDto=new ArticleDeleteDto(Id,"삭제에 실패했습니다.");
        ArticleDeleteDto articleDeleteDto1=new ArticleDeleteDto(Id,"삭제가 완료되었습니다.");

        if(email.equals(articles.getMember().getEmail())){
            articleRepository.delete(article);
//            imageRepository.deleteById(Id);
            return articleDeleteDto1;
        }else return articleDeleteDto;

    }
}





