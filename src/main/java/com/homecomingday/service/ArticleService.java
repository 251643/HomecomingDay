package com.homecomingday.service;


import com.homecomingday.controller.S3Dto;
import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.*;
import com.homecomingday.domain.*;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.repository.HeartRepository;
import com.homecomingday.repository.ImageRepository;
import com.homecomingday.service.s3.S3Uploader;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final HeartRepository heartRepository;


    //현재 게시물 받아오기
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalPost = articleRepository.findById(id);
        return optionalPost.orElse(null);
    }



    //검색창 페이지 목록조회
    public List<GetAllArticleDto> searchArticle(){
        List<Article> articleList = articleRepository.findAll();

        List<GetAllArticleDto> getAllArticleDtoList= new ArrayList<>();

        for(Article findArticle : articleList){

            if (!findArticle.getArticleFlag().equals("calendar")) { //만남일정 부분 제외하고 모든값 출력
                List<Image> findImage = imageRepository.findAll();
                List<ImagePostDto> pickImage = new ArrayList<>();

                for (Image image : findImage) {
                    if (image.getArticle().getId().equals(findArticle.getId())) {
                        pickImage.add(
                                ImagePostDto.builder()
                                        .imageId(image.getId())
                                        .imgUrl(image.getImgUrl())
                                        .build()
                        );
                    }
                }

                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .imageList(pickImage)
                                .username(findArticle.getMember().getUsername())
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentname())
                                .articleFlag(findArticle.getArticleFlag())
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .build()
                );
            } else { //만남일정 부분  출력
                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .calendarDate(findArticle.getCalendarDate())
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .username(findArticle.getMember().getUsername())
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentname())
                                .articleFlag(findArticle.getArticleFlag())
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .build()

                );
            }
        }
        return getAllArticleDtoList;
    }


    //메인페이지 게시물 조회
    public List<GetAllArticleDto> readAllArticle(String articleFlag) {

        List<Article> articleList = articleRepository.findByArticleFlagOrderByCreatedAtDesc(articleFlag);

        List<GetAllArticleDto> getAllArticleDtoList = new ArrayList<>();

        for (Article findArticle : articleList) {
            List<Comment> commentList = findArticle.getComments(); //게시물 index 번호에 따라 뽑아옴
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();   //for문 안에 있어야 계속 초기화돼서 들어감

            for (Comment comment : commentList) {

                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .userImage(changeImage(comment.getMember().getUserImage()))
                                .admission(comment.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(comment.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .build()
                );
            }

//            List<Comment>findComment =commentRepository.findAll();
//        List<ImagePostDto> imageList = new ArrayList<>();

            if (!articleFlag.equals("calendar")) { //만남일정 부분 제외하고 모든값 출력
                List<Image> findImage = imageRepository.findAll();
                List<ImagePostDto> pickImage = new ArrayList<>();

                for (Image image : findImage) {
                    if (image.getArticle().getId().equals(findArticle.getId())) {
                        pickImage.add(
                                ImagePostDto.builder()
                                        .imageId(image.getId())
                                        .imgUrl(image.getImgUrl())
                                        .build()
                        );
                    }
                }

                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .imageList(pickImage)
                                .username(findArticle.getMember().getUsername())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentname())
                                .articleFlag(articleFlag)
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .commentCnt((long) commentResponseDtoList.size())
                                .commentList(commentResponseDtoList)
                                .build()
                );
            } else { //만남일정 부분  출력
                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .calendarDate(findArticle.getCalendarDate())
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .username(findArticle.getMember().getUsername())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentname())
                                .articleFlag(articleFlag)
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .commentCnt((long) commentResponseDtoList.size())
                                .commentList(commentResponseDtoList)
                                .build()

                );


            }
        }


        return getAllArticleDtoList;
    }


    //게시글 만남일정이랑 다른친구 구분값으로 api주던가 flag통해서 구분값주기
    //게시글 생성
    public ArticleResponseDto postArticle(String articleFlag,
                                          List<MultipartFile> multipartFile,
                                          ArticleRequestDto articleRequestDto,
                                          UserDetailsImpl userDetails) throws IOException {


        System.out.println(userDetails);
        Article article = Article.builder()
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .views(0L)
                .member(userDetails.getMember())
                .articleFlag(articleFlag)
                .calendarDate(articleRequestDto.getCalendarDate())
                .calendarTime(articleRequestDto.getCalendarTime())
                .calendarLocation(articleRequestDto.getCalendarLocation())
                .build();
        articleRepository.save(article);


        if (!articleFlag.equals("calendar")) { //만남일정만 제외하고 이 부분에서 true시에 출력

            int checkNum = 1; // 이미지 if(uploadedFile.isEmpty()) 비교를 위해 선언
            // 조건문을 통과하면 안에 값이 비어있다는것, 리스트자체가 아닌 내부 값 자체를 비교해야함
            List<ImagePostDto> imgbox = new ArrayList<>();
//            이미지 null값 비교값
            for (MultipartFile uploadedFile : multipartFile) {
                if (uploadedFile.isEmpty()) //multipartFile을 비교할때는 isEmpty()를 통해서 비교해야함
                    checkNum = 0;
            }


            if (checkNum==1) { //이미지 있을때 출력 로직
                //이미지 업로드
                for (MultipartFile uploadedFile : multipartFile) {
                    S3Dto s3Dto = s3Uploader.upload(uploadedFile);

                    Image image = Image.builder()
                            .imgUrl(s3Dto.getUploadImageUrl())
                            .urlPath(s3Dto.getFileName())
                            .article(article)
                            .member(article.getMember())
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
            } else { //이미지 없을때 출력 로직


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

                return articleResponseDto;
            }
        } else { //calendar 부분만 출력

            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .username(article.getMember().getUsername())
                    .content(article.getContent())
                    .calendarDate(article.getCalendarDate())
                    .calendarTime(article.getCalendarTime())
                    .calendarLocation(article.getCalendarLocation())
                    .articleFlag(articleFlag)
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentname())
                    .views(0L)
                    .commentCnt(0L) // 0으로 기본세팅
                    .build();


            return articleResponseDto;
        }

    }


    // 게시글 상세 조회
    @Transactional            //get이라고 @transactional 지우지마세요 조회수 갱신때메 넣어야합니다.빼면 에러나유
    public ArticleResponseDto readArticle(String articleFlag, Long articleId, UserDetailsImpl userDetails) {
        articleRepository.updateCount(articleId);

        Article article = isPresentArticle(articleId);
        if (null == article) {
            throw new RuntimeException("해당 게시글이 없습니다.");
        }

        List<Comment> findComment = commentRepository.findAll();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        List<ImagePostDto> imageList = new ArrayList<>();

        for (Comment comment : findComment) {
            if (comment.getArticle().getId().equals(articleId)) {
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .userImage(changeImage(comment.getMember().getUserImage()))
                                .admission(comment.getMember().getAdmission().substring(2, 4)+"학번")
                                .departmentName(comment.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .articleId(articleId)
                                .build()
                );
            }
        }
        if (!articleFlag.equals("calendar")) { //조건문을 통해 정보공유,선배님도와주세요,자유게시판에만 이미지값이 출력

            List<Image> findImage = imageRepository.findAll();
            List<ImagePostDto> pickImage = new ArrayList<>();

            for (Image image : findImage) {
                if (image.getArticle().getId().equals(articleId)) {
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
                    .userImage(changeImage(article.getMember().getUserImage()))
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentname())
                    .views(article.getViews())
                    .heartCnt( article.getHeartCnt())
                    .imageList(pickImage)
                    .commentCnt((long) commentResponseDtoList.size())
                    .commentList(commentResponseDtoList)
                    .build();
        } else { //calendar 임시 출력 mDate/mTime/place 추가예정 (프론트와 합의하에 임시적으로 이부분만 출력)
            return ArticleResponseDto.builder()
                    .articleId(article.getId())
                    .articleFlag(articleFlag)
                    .title(article.getTitle())
                    .content(article.getContent())
                    .calendarDate(article.getCalendarDate())
                    .calendarTime(article.getCalendarTime())
                    .calendarLocation(article.getCalendarLocation())
                    .username(article.getMember().getUsername())
                    .userImage(changeImage(article.getMember().getUserImage()))
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentname())
                    .views(article.getViews())
                    .heartCnt( article.getHeartCnt())
                    .commentCnt((long) commentResponseDtoList.size())
                    .commentList(commentResponseDtoList)
                    .build();
        }


    }


    //게시글 수정 ---사진수정은 안하기로함
    @Transactional
    public String updateArticles(String articleFlag, Long articleId,
                                 UserDetailsImpl userDetails,
                                 ArticleRequestDto articleRequestDto) {
        Article article = isPresentArticle(articleId);
        String myId = String.valueOf(articleId);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<ImagePostDto> imageList = new ArrayList<>();

        for (Comment comment : article.getComments()) {
            if (comment.getArticle().getId().equals(articleId)) {
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .admission(comment.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(comment.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .build()
                );
            }
        }



        if (userDetails.getUsername().equals(article.getMember().getEmail())) { //유니크 처리를 email만 해줬기에 기존 작성자와 현로그인한 유저의 이메일을 비교하여 바꿔준다

            article.updateArticle(articleRequestDto);


            return articleFlag + "  " + myId + "번 게시글이 수정";
        }

        return articleFlag + "  " + myId + "번 게시글이 수정 실패";
    }


    //게시글 삭제
    public String deleteArticles(String articleFlag, Long articleId, UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));

        Article articles = isPresentArticle(articleId);
        String email = userDetails.getUsername();
        String myId = String.valueOf(articleId);


        if (email.equals(articles.getMember().getEmail())) {

            articleRepository.delete(article);
            return articleFlag + " " + myId + "번 게시글이 삭제 성공";
        } else return articleFlag + " " + myId + "번 게시글이 삭제 실패";

    }

    //게시글 좋아요
    @Transactional
    public String heartArticle(Long articleId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        if(heartRepository.findByMemberAndArticle(member, article) == null){
            Heart heart = new Heart(member, article);
            article.addHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.save(heart);
            return  "총 좋아요 수 : " + article.getHeartCnt();
        }else  {
            Heart heart = heartRepository.findByMemberAndArticle(member, article);
            article.removeHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.delete(heart);
            return "총 좋아요 수 : " + article.getHeartCnt();
        }
    }

    private String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/e778fd8b-8761-444f-8f0a-8f7cb56c3854%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF.png";

        }else {
            return userImage;
        }

    }
    }





