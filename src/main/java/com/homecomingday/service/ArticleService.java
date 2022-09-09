package com.homecomingday.service;


import com.homecomingday.controller.S3Dto;
import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.*;
import com.homecomingday.domain.*;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;


    //현재 게시물 받아오기
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalPost = articleRepository.findById(id);
        return optionalPost.orElse(null);
    }


    //메인페이지 게시물 조회
    public List<GetAllArticleDto> readAllArticle(String articleFlag) {

        List<Article> articleList = articleRepository.findByArticleFlagOrderByCreatedAtDesc(articleFlag);

        List<GetAllArticleDto> getAllArticleDtos = new ArrayList<>();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Article findarticle : articleList) {
            Long sizeCnt = 0L;
            List<Comment> commentList = commentRepository.findbyArticle_Id(findarticle.getId()); //게시물 index 번호에 따라 뽑아옴

//            for (Comment datas : commentList) {
//                if (datas.getArticle().getId().equals(findarticle.getId())) {
//                    sizeCnt++;
//                }
//            }

//            List<Comment>findComment =commentRepository.findAll();
//        List<ImagePostDto> imageList = new ArrayList<>();

            for (Comment comment : commentList) {
                if(comment.getArticle().getId().equals(findarticle.getId())) {
                    commentResponseDtoList.add(
                            CommentResponseDto.builder()
                                    .commentId(comment.getId())
                                    .content(comment.getContent())
                                    .username(comment.getMember().getUsername())
                                    .admission(comment.getMember().getAdmission())
                                    .departmentName(comment.getMember().getDepartmentname())
                                    .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                    .build()
                    );
                }
            }


            List <Image> findImage=imageRepository.findAll();
            List <ImagePostDto> pickImage=new ArrayList<>();

            for (Image image : findImage) {
                if(image.getArticle().getId().equals(findarticle.getId())) {
                    pickImage.add(
                            ImagePostDto.builder()
                                    .imageId(image.getId())
                                    .imgUrl(image.getImgUrl())
                                    .build()
                    );
                }
            }

            getAllArticleDtos.add(
                    GetAllArticleDto.builder()
                            .articleId(findarticle.getId())
                            .title(findarticle.getTitle())
                            .content(findarticle.getContent())
                            .imageList(pickImage)
                            .username(findarticle.getMember().getUsername())
                            .createdAt(Time.convertLocaldatetimeToTime(findarticle.getCreatedAt()))
                            .admission(findarticle.getMember().getAdmission().substring(2, 4) + "학번")
                            .departmentName(findarticle.getMember().getDepartmentname())
                            .articleFlag(articleFlag)
                            .views(findarticle.getViews())
                            .commentCnt((long) commentResponseDtoList.size())
                            .commentList(commentResponseDtoList)
                            .build()
            );
        }
        return getAllArticleDtos;
    }


    //게시글 만남일정이랑 다른친구 구분값으로 api주던가 flag통해서 구분값주기
    //게시글 생성
    public ArticleResponseDto postArticle(String articleFlag,
                                          List<MultipartFile> multipartFile,
                                          ArticleRequestDto articleRequestDto,
                                          UserDetailsImpl userDetails,
                                          HttpServletRequest request) throws IOException {


        System.out.println(userDetails);
        Article article = Article.builder()
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .views(0L)
                .member(userDetails.getMember())
                .articleFlag(articleFlag)
                .build();
        articleRepository.save(article);


//        if(!articleFlag.equals("information")) {

            int checkNum = 1; // 이미지 if(uploadedFile.isEmpty()) 비교를 위해 선언
            // 조건문을 통과하면 안에 값이 비어있다는것, 리스트자체가 아닌 내부 값 자체를 비교해야함
            List<ImagePostDto> imgbox = new ArrayList<>();

            //이미지 null값 비교값
            for (MultipartFile uploadedFile : multipartFile) {
                if (uploadedFile.isEmpty()) //multipartFile을 비교할때는 isEmpty()를 통해서 비교해야함
                    checkNum = 0;
            }
            if (checkNum == 1) {
                //이미지 업로드
                for (MultipartFile uploadedFile : multipartFile) {
                    S3Dto s3Dto = s3Uploader.upload(uploadedFile);

                    Image image = Image.builder()
                            .imgUrl(s3Dto.getUploadImageUrl())
                            .urlPath(s3Dto.getFileName())
                            .article(article)
                            .build();
                    imageRepository.save(image);

                    ImagePostDto imagePostDto = ImagePostDto.builder()
                            .imageId(image.getId())
                            .imgUrl(image.getImgUrl())
                            .build();

                    imgbox.add(imagePostDto);

                }
                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .articleId(article.getId())
                        .articleFlag(articleFlag)
                        .title(article.getTitle())
                        .content(article.getContent())
                        .username(article.getMember().getUsername())
                        .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                        .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                        .departmentName(article.getMember().getDepartmentname())
                        .views(0L)
                        .imageList(imgbox)
                        .commentCnt(0L) // 0으로 기본세팅
                        .build();

//            String admission1=userDetails.getMember().getAdmission().substring(2,4)+"학번";
//            Article2ResponseDto article2ResponseDto=
//                    new Article2ResponseDto(article,articleFlag,admission1,imgbox);
                return articleResponseDto;
            } else {


                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .articleId(article.getId())
                        .articleFlag(articleFlag)
                        .title(article.getTitle())
                        .content(article.getContent())
                        .username(article.getMember().getUsername())
                        .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                        .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                        .departmentName(article.getMember().getDepartmentname())
                        .views(0L)
                        .commentCnt(0L) // 0으로 기본세팅
                        .build();
//            String admission1=userDetails.getMember().getAdmission().substring(2,4)+"학번";

//            Article2ResponseDto article2ResponseDto=
//                    new Article2ResponseDto(article,articleFlag,admission1);
                return articleResponseDto;
            }
//        }
//        else{
//
//        }

}






    // 게시글 상세 조회
    @Transactional            //get이라고 @transactional 지우지마세요 조회수 갱신때메 넣어야합니다.빼면 에러나유
    public ArticleResponseDto readArticle(String articleFlag,Long articleId,UserDetailsImpl userDetails) {
        articleRepository.updateCount(articleId); //현재는 쿠키값 상관없이 조회수 누적체크

        Article article = isPresentArticle(articleId);
        if (null == article) {
            throw new RuntimeException("해당 게시글이 없습니다.");
        }

        List<Comment>findComment =commentRepository.findAll();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        List<ImagePostDto> imageList = new ArrayList<>();

        for (Comment comment : findComment) {
            if(comment.getArticle().getId().equals(articleId)) {
                    commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .admission(comment.getMember().getAdmission())
                                .departmentName(comment.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .build()
                );
            }
        }
        List <Image> findImage=imageRepository.findAll();
        List <ImagePostDto> pickImage=new ArrayList<>();

            for (Image image : findImage) {
                if(image.getArticle().getId().equals(articleId)) {
                    pickImage.add(
                            ImagePostDto.builder()
                                    .imageId(image.getId())
                                    .imgUrl(image.getImgUrl())
                                    .build()
                    );
                }
            }

        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .articleFlag(articleFlag)
                .title(article.getTitle())
                .content(article.getContent())
                .username(article.getMember().getUsername())
                .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                .admission(article.getMember().getAdmission().substring(2,4)+"학번")
                .departmentName(article.getMember().getDepartmentname())
                .views(article.getViews())
                .imageList(pickImage)
                .commentCnt((long) commentResponseDtoList.size())
                .commentList(commentResponseDtoList)
                .build();
    }


    //게시글 수정 ---사진수정은 안하기로함
    @Transactional
    public String updateArticles( String articleFlag,Long articleId,
                                            UserDetailsImpl userDetails,
                                            ArticleRequestDto articleRequestDto) {
        Article article = isPresentArticle(articleId);
        String myId = String.valueOf(articleId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<ImagePostDto> imageList = new ArrayList<>();

        for (Comment comment : article.getComments()) {
            if(comment.getArticle().getId().equals(articleId)){
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .admission(comment.getMember().getAdmission())
                                .departmentName(comment.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .build()
                );
            }
        }


//        for (Image image : article.getImageList()) {
//            imageList.add(
//                    ImagePostDto.builder()
//                            .imageId(image.getId())
//                            .imgUrl(image.getImgUrl())
//                            .build()
//            );
//        }


//        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
//                .articleId(article.getId())
//                .title(articleRequestDto.getTitle())
//                .content(articleRequestDto.getContent())
//                .username(userDetails.getMember().getUsername())
//                .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
//                .admission(article.getMember().getAdmission())
//                .views(article.getViews())
//                .commentCnt((long) commentResponseDtoList.size())
//                .build();

        if (userDetails.getUsername().equals(article.getMember().getEmail())) { //유니크 처리를 email만 해줬기에 기존 작성자와 현로그인한 유저의 이메일을 비교하여 바꿔준다

            article.updateArticle(articleRequestDto);


            return articleFlag+"  "+myId+"번 게시글이 수정";
        }

        return articleFlag+"  "+myId+"번 게시글이 수정 실패";
    }


    //게시글 삭제
    public String deleteArticles(String articleFlag,Long articleId,UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));

        Article articles = isPresentArticle(articleId);
        String email=userDetails.getUsername();
        String myId = String.valueOf(articleId);



        if(email.equals(articles.getMember().getEmail())){

            articleRepository.delete(article);
            return articleFlag+" "+myId+"번 게시글이 삭제 성공";
        }else return articleFlag+" "+myId+"번 게시글이 삭제 실패";

    }
}





