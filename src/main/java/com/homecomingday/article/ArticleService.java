package com.homecomingday.article;


import com.homecomingday.article.requestDto.ImagePostDto;
import com.homecomingday.article.responseDto.*;
import com.homecomingday.comment.CommentRepository;
import com.homecomingday.comment.CommitRepository;
import com.homecomingday.comment.responseDto.CommentResponseDto;
import com.homecomingday.comment.responseDto.CommitResponseDto;
import com.homecomingday.dto.S3Dto;
import com.homecomingday.article.requestDto.ArticleRequestDto;
import com.homecomingday.domain.*;
import com.homecomingday.notification.NotificationService;
import com.homecomingday.shared.s3.S3Uploader;
import com.homecomingday.util.ArticleChange;
import com.homecomingday.util.CacheKey;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;
    private final CommitRepository commitRepository;
    private final ImageRepository imageRepository;
    private final HeartRepository heartRepository;

    private final NotificationService notificationService;

    private final ParticipantRepository participantRepository;
    private final CacheSevice cacheSevice;


    //현재 게시물 받아오기
    @Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalPost = articleRepository.findById(id);
        return optionalPost.orElse(null);
    }


    //검색창 페이지 인기 목록조회 수정했슴다
    @Cacheable(value = CacheKey.SEARCHPOP, key = "#userDetails.getMember().getSchoolName()", unless = "#result == null")
    public List<GetAllArticleDto> searchPopularArticle(UserDetailsImpl userDetails){

        List<Article> articleList = articleRepository.findBySchoolNameOrderByViewsDesc(userDetails.getMember().getSchoolName());
        List<GetAllArticleDto> getAllArticleDtoList= new ArrayList<>();

        for(Article findArticle : articleList){

            if (!findArticle.getArticleFlag().equals("calendar")) { //만남일정 부분 제외하고 모든값 출력

                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(findArticle.getArticleFlag()))
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .commentCnt((long) findArticle.getComments().size())
                                .build()
                );
            } else { //만남일정 부분  출력
                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .calendarDate(ArticleChange.changeCalendarDate(findArticle.getCalendarDate()))
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(findArticle.getArticleFlag()))
                                .views(findArticle.getViews())
                                .heartCnt(findArticle.getHeartCnt())
                                .commentCnt((long) findArticle.getComments().size())
                                .joinCheck(ArticleChange.changeJoinPeople(findArticle))
                                .build()
                );
            }
        }
        return getAllArticleDtoList;
    }

    //검색창 페이지 목록조회
    @Cacheable(value = CacheKey.SERACH, key = "#userDetails.getMember().getSchoolName()", unless = "#result == null")
    public List<GetAllArticleDto> searchArticle(UserDetailsImpl userDetails){
        List<Article> articleList = articleRepository.findBySchoolNameOrderByCreatedAtDesc(userDetails.getMember().getSchoolName());

        List<GetAllArticleDto> getAllArticleDtoList= new ArrayList<>();

        for(Article findArticle : articleList){


            if (!findArticle.getArticleFlag().equals("calendar")) { //만남일정 부분 제외하고 모든값 출력

                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(findArticle.getArticleFlag()))
                                .views(findArticle.getViews())
                                .heartCnt(findArticle.getHeartCnt())
                                .commentCnt((long) findArticle.getComments().size())
                                .build()
                );
            } else { //만남일정 부분  출력
                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .calendarDate(ArticleChange.changeCalendarDate(findArticle.getCalendarDate()))
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(findArticle.getArticleFlag()))
                                .views(findArticle.getViews())
                                .heartCnt(findArticle.getHeartCnt())
                                .commentCnt((long) findArticle.getComments().size())
                                .joinCheck(ArticleChange.changeJoinPeople(findArticle))
                                .build()
                );
            }
        }
        return getAllArticleDtoList;
    }

    //메인페이지 인기순 조회
    @Cacheable(value = CacheKey.ARTICLEPOP, key = "#articleFlag + #userDetails.getMember().getSchoolName()", unless = "#result == null")
    public List<GetAllArticleDto> readPopularArticle(String articleFlag, UserDetailsImpl userDetails){

        List<Article> articleList = articleRepository.findByArticleFlagAndSchoolNameOrderByViewsDesc(articleFlag,userDetails.getMember().getSchoolName());
        List<GetAllArticleDto> getAllArticleDtoList = new ArrayList<>();

        for (Article findArticle : articleList) {


            if (!articleFlag.equals("calendar")) { //만남일정 부분 제외하고 모든값 출력

                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                .commentCnt((long) findArticle.getComments().size())
                                .build()
                );
            } else { //만남일정 부분  출력
                getAllArticleDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .calendarDate(ArticleChange.changeCalendarDate(findArticle.getCalendarDate()))
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .content(findArticle.getContent())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .email(findArticle.getMember().getEmail())
                                .userImage(changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                .views(findArticle.getViews())
                                .heartCnt( findArticle.getHeartCnt())
                                .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                .commentCnt((long) findArticle.getComments().size())
                                .joinCheck(ArticleChange.changeJoinPeople(findArticle))
                                .build()
                );
            }
        }

        return getAllArticleDtoList;
    }


    //메인페이지 게시물 조회
    @Cacheable(value = CacheKey.ARTICLES, key = "#articleFlag + #userDetails.getMember().getSchoolName()", unless = "#result == null")
    public List<GetAllArticleDto> readAllArticle(String articleFlag, UserDetailsImpl userDetails) {

        List<Article> articleList = articleRepository.findByArticleFlagAndSchoolNameOrderByCreatedAtDesc(articleFlag, userDetails.getMember().getSchoolName());

        List<GetAllArticleDto> getAllArticleDtoList = new ArrayList<>();


        for (Article findArticle : articleList) {


                if (!articleFlag.equals("calendar")) { //만남일정 부분 제외하고 모든값 출력

                    getAllArticleDtoList.add(
                            GetAllArticleDto.builder()
                                    .articleId(findArticle.getId())
                                    .title(findArticle.getTitle())
                                    .content(findArticle.getContent())
                                    .username(findArticle.getMember().getUsername())
                                    .userId(findArticle.getMember().getId())
                                    .email(findArticle.getMember().getEmail())
                                    .userImage(changeImage(findArticle.getMember().getUserImage()))
                                    .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                    .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                    .departmentName(findArticle.getMember().getDepartmentName())
                                    .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                    .views(findArticle.getViews())
                                    .heartCnt(findArticle.getHeartCnt())
                                    .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                    .commentCnt((long) findArticle.getComments().size())
                                    .build()
                    );
                } else { //만남일정 부분  출력
                    getAllArticleDtoList.add(
                            GetAllArticleDto.builder()
                                    .articleId(findArticle.getId())
                                    .title(findArticle.getTitle())
                                    .calendarDate(ArticleChange.changeCalendarDate(findArticle.getCalendarDate()))
                                    .calendarTime(findArticle.getCalendarTime())
                                    .calendarLocation(findArticle.getCalendarLocation())
                                    .username(findArticle.getMember().getUsername())
                                    .userId(findArticle.getMember().getId())
                                    .email(findArticle.getMember().getEmail())
                                    .userImage(changeImage(findArticle.getMember().getUserImage()))
                                    .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                    .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                    .departmentName(findArticle.getMember().getDepartmentName())
                                    .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                    .views(findArticle.getViews())
                                    .heartCnt(findArticle.getHeartCnt())
                                    .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                    .commentCnt((long) findArticle.getComments().size())
                                    .joinCheck(ArticleChange.changeJoinPeople(findArticle))
                                    .build()
                    );
                }
            }

        return getAllArticleDtoList;
    }


    //게시글 만남일정이랑 다른친구 구분값으로 api주던가 flag통해서 구분값주기
    //게시글 생성
    @CacheEvict(value = CacheKey.ARTICLES, key = "#articleFlag + #userDetails.getMember().getSchoolName()")
    public ArticleResponseDto postArticle(String articleFlag,
                                          List<MultipartFile> multipartFile,
                                          ArticleRequestDto articleRequestDto,
                                          UserDetailsImpl userDetails) throws IOException {

        Article article = Article.builder()
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .views(0L)
                .member(userDetails.getMember())
                .articleFlag(articleFlag)
                .calendarDate(articleRequestDto.getCalendarDate())
                .calendarTime(articleRequestDto.getCalendarTime())
                .calendarLocation(articleRequestDto.getCalendarLocation())
                .maxPeople(articleRequestDto.getMaxPeople())
                .schoolName(userDetails.getMember().getSchoolName())
                .build();
        articleRepository.save(article);


        if (!articleFlag.equals("calendar")) { //만남일정만 제외하고 이 부분에서 true시에 출력

            List<ImagePostDto> imgbox = new ArrayList<>();

            if (!(multipartFile==null)) { //이미지 있을때 출력 로직
                //이미지 업로드
                int checkNum =1;
                for(MultipartFile image:multipartFile){
                    if(image.isEmpty()) checkNum=0;
                }
                if (checkNum==1) { //이미지 있을때 출력 로직a
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
                            .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                            .title(article.getTitle())
                            .content(article.getContent())
                            .username(article.getMember().getUsername())
                            .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                            .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                            .departmentName(article.getMember().getDepartmentName())
                            .views(0L)
                            .imageList(imgbox)
                            .isHeart(false)
                            .commentCnt(0L) // 0으로 기본세팅
                            .build();

                    return articleResponseDto;
                } else { //이미지 없을때 출력 로직


                    ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                            .articleId(article.getId())
                            .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                            .title(article.getTitle())
                            .content(article.getContent())
                            .username(article.getMember().getUsername())
                            .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                            .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                            .departmentName(article.getMember().getDepartmentName())
                            .views(0L)
                            .isHeart(false)
                            .commentCnt(0L) // 0으로 기본세팅
                            .build();

                    return articleResponseDto;
                }
            } else { //이미지 없을때 출력 로직


                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .articleId(article.getId())
                        .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                        .title(article.getTitle())
                        .content(article.getContent())
                        .username(article.getMember().getUsername())
                        .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                        .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                        .departmentName(article.getMember().getDepartmentName())
                        .views(0L)
                        .isHeart(false)
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
                    .calendarDate(ArticleChange.changeCalendarDate(article.getCalendarDate()))
                    .calendarTime(article.getCalendarTime())
                    .calendarLocation(article.getCalendarLocation())
                    .maxPeople(article.getMaxPeople())
                    .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentName())
                    .views(0L)
                    .isHeart(false)
                    .commentCnt(0L) // 0으로 기본세팅
                    .build();


            return articleResponseDto;
        }

    }


    // 게시글 상세 조회
    public ArticleResponseDto readArticle(String articleFlag, Long articleId, UserDetailsImpl userDetails) {

        Article article = isPresentArticle(articleId);
        if (null == article) {
            throw new RuntimeException("해당 게시글이 없습니다.");
        }

        List<Comment> findComment = article.getComments();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : findComment) {

            List<Commit> commitList = commitRepository.findByCommentAndArticle(comment,article);
            List<CommitResponseDto> commitResponseDtoList = new ArrayList<>();
            for (Commit commit : commitList) {
                commitResponseDtoList.add(
                        CommitResponseDto.builder()
                                .childCommentId(commit.getId())
                                .content(commit.getContent())
                                .username(commit.getMember().getUsername())
                                .userId(commit.getMember().getId())
                                .email(commit.getMember().getEmail())
                                .userImage(changeImage(commit.getMember().getUserImage()))
                                .admission(commit.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(commit.getMember().getDepartmentName())
                                .createdAt(Time.convertLocaldatetimeToTime(commit.getCreatedAt()))
                                .build()
                );
            }

            if (comment.getArticle().getId().equals(articleId)) {
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .userId(comment.getMember().getId())
                                .email(comment.getMember().getEmail())
                                .userImage(changeImage(comment.getMember().getUserImage()))
                                .admission(comment.getMember().getAdmission().substring(2, 4)+"학번")
                                .departmentName(comment.getMember().getDepartmentName())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .childCommentList(commitResponseDtoList)
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
                    .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                    .title(article.getTitle())
                    .content(article.getContent())
                    .username(article.getMember().getUsername())
                    .userId(article.getMember().getId())
                    .email(article.getMember().getEmail())
                    .userImage(changeImage(article.getMember().getUserImage()))
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentName())
                    .views(article.getViews())
                    .heartCnt( article.getHeartCnt())
                    .imageList(pickImage)
                    .isHeart(heartCheck(article, userDetails.getMember()))
                    .commentCnt((long) commentResponseDtoList.size())
                    .commentList(commentResponseDtoList)
                    .build();
        } else { //calendar 파트만 출력
            return ArticleResponseDto.builder()
                    .articleId(article.getId())
                    .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                    .title(article.getTitle())
                    .content(article.getContent())
                    .calendarDate(ArticleChange.changeCalendarDate(article.getCalendarDate()))
                    .calendarTime(article.getCalendarTime())
                    .calendarLocation(article.getCalendarLocation())
                    .maxPeople(article.getMaxPeople())
                    .username(article.getMember().getUsername())
                    .userId(article.getMember().getId())
                    .email(article.getMember().getEmail())
                    .userImage(changeImage(article.getMember().getUserImage()))
                    .createdAt(Time.convertLocaldatetimeToTime(article.getCreatedAt()))
                    .admission(article.getMember().getAdmission().substring(2, 4) + "학번")
                    .departmentName(article.getMember().getDepartmentName())
                    .views(article.getViews())
                    .heartCnt(article.getHeartCnt())
                    .isHeart(heartCheck(article, userDetails.getMember()))
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
                                .departmentName(comment.getMember().getDepartmentName())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .build()
                );
            }
        }


        if(!articleFlag.equals("calendar")) {
            if (userDetails.getUsername().equals(article.getMember().getEmail())) { //유니크 처리를 email만 해줬기에 기존 작성자와 현로그인한 유저의 이메일을 비교하여 바꿔준다
                article.updateArticle(articleRequestDto);

                cacheSevice.deleteBoardCache(article.getId(), userDetails, articleFlag);
                return articleFlag + "  " + myId + "번 게시글 수정";

            }

        } else { //글의 종류가 만남일정인 경우에만
            if (userDetails.getUsername().equals(article.getMember().getEmail())) { //유니크 처리를 email만 해줬기에 기존 작성자와 현로그인한 유저의 이메일을 비교하여 바꿔준다
               if(articleRequestDto.getMaxPeople()>=article.getParticipants().size()) {
                   article.updateArticle(articleRequestDto);

                   cacheSevice.deleteBoardCache(article.getId(), userDetails, articleFlag);
                   return articleFlag + "  " + myId + "번 게시글 수정";
               }
            }

        }
        return articleFlag + "  " + myId + "번 게시글 수정 실패";
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

            cacheSevice.deleteBoardCache(article.getId(), userDetails, articleFlag);
            return articleFlag + " " + myId + "번 게시글이 삭제 성공";
        } else return articleFlag + " " + myId + "번 게시글이 삭제 실패";

    }

    //게시글 좋아요
    @Transactional
    public boolean heartArticle(Long articleId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        if(heartRepository.findByMemberAndArticle(member, article) == null){
            Heart heart = new Heart(member, article);
            article.addHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.save(heart);
            //댓글 채택 시 채택된 댓글 유저에게 실시간 알림 전송
//            String message = article.getMember().getUsername() + "님! 게시글에 좋아요가 달렸어요~" ;
//
            long now = ChronoUnit.MINUTES.between(heart.getCreatedAt() , LocalDateTime.now());
            Time time = new Time();
            String createdAt = time.times(now);

            //본인의 게시글에 댓글을 남길때는 알림을 보낼 필요가 없다.
            if(!Objects.equals(heart.getMember().getId(), article.getMember().getId())) {
                notificationService.send(article.getMember(), NoticeType.heart, article.getId(), article.getTitle(),createdAt,heart);
            }

            return true;
        }else  {
            Heart heart = heartRepository.findByMemberAndArticle(member, article);
            article.removeHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.delete(heart);
            return  false;
        }
    }
    //좋아요 확인
    public ConfirmHeartDto confirmHeart(Long articleId, UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        return ConfirmHeartDto.builder()
                .heartCnt(article.getHeartCnt())
                .isHeart(heartCheck(article, userDetails.getMember()))
                .build();


    }

    ///게시글 좋아요 확인
    public boolean heartCheck(Article article, Member member) {
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return heartRepository.existsByMemberAndArticle(member, article);

    }

    private String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/52a5f48d-7452-48a6-b167-1497931c7cf7%ED%94%84%EB%A1%9C%ED%95%84%20%EC%9D%B4%EB%AF%B8%EC%A7%80%20%EC%A0%80%EC%9E%A5%EC%9A%A9.png";

        }else {
            return userImage;
        }

    }



    //참여하기 버튼
    @Transactional
    public CheckJoinDto checkJoin(Long articleId, UserDetailsImpl userDetails, String email) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        Member loginMember = userDetails.getMember();

        List<Participant> checkParticipant = participantRepository.findAllByMember(loginMember);
        if(article.getParticipants().size()<article.getMaxPeople()){ //모집인원이 충족되지 않았을때
                if(participantRepository.findByMemberAndArticle(loginMember,article) == null){ //참여를 누르지 않았을때
                    Participant newParticipant=new Participant(loginMember,article);
                    article.addParticipant(newParticipant);
                    participantRepository.save(newParticipant);

                    return CheckJoinDto.builder()
                            .email(userDetails.getUsername())
                            .userImage(userDetails.getMember().getUserImage())
                            .username(userDetails.getMember().getUsername())
                            .department(userDetails.getMember().getDepartmentName())
                            .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                            .joinCheck(true)
                            .joinPeople(article.getParticipants().size())
                            .build();
                }else{//기존에 참여를 누른 상태에서 한번더 눌렀을 때
                    Participant participant=participantRepository.findByMemberAndArticle(loginMember,article);
                    article.removeParticipant(participant);
                    participantRepository.delete(participant);

                    return CheckJoinDto.builder()
                            .email(userDetails.getUsername())
                            .userImage(userDetails.getMember().getUserImage())
                            .username(userDetails.getMember().getUsername())
                            .department(userDetails.getMember().getDepartmentName())
                            .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                            .joinCheck(false)
                            .joinPeople(article.getParticipants().size())
                            .build();
                }
        }else if(article.getParticipants().size()==article.getMaxPeople()) { //모집인원이 참여인원이 동일할때
                if(participantRepository.findByMemberAndArticle(loginMember,article) == null) {
                    throw new IllegalArgumentException("인원이 충족되어 참여할 수 없습니다");
                }else{
                    Participant participant=participantRepository.findByMemberAndArticle(loginMember,article);
                    article.removeParticipant(participant);
                    participantRepository.delete(participant);

                    return CheckJoinDto.builder()
                            .email(userDetails.getUsername())
                            .userImage(userDetails.getMember().getUserImage())
                            .username(userDetails.getMember().getUsername())
                            .department(userDetails.getMember().getDepartmentName())
                            .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                            .joinCheck(false)
                            .joinPeople(article.getParticipants().size())
                            .build();
                }
        }else

        return null;
    }

  //참여하기 인원 조회
    public CheckAllParticipantDto checkJoinPeople(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
       List<joinPeopleDto> joinPeopleDtos=new ArrayList<>();

        for(Participant joinPeop : article.getParticipants()){
            joinPeopleDtos.add(
                    joinPeopleDto.builder()
                            .email(joinPeop.getMember().getEmail())
                            .userImage(ArticleChange.changeImage(joinPeop.getMember().getUserImage()))
                            .username(joinPeop.getMember().getUsername())
                            .department(joinPeop.getMember().getDepartmentName())
                            .admission(joinPeop.getMember().getAdmission().substring(2, 4) + "학번")
                            .joinCheck(true)
                            .build()
            );
        }
        return CheckAllParticipantDto.builder()
                .articleId(articleId)
                .joinPeople(article.getParticipants().size())
                .joinPeopleList(joinPeopleDtos)
                .build();



    }


    @Transactional
    public ViewResponseDto checkViews(Long articleId) {
        articleRepository.updateCount(articleId);
        Article article = isPresentArticle(articleId);

        return new ViewResponseDto(articleId,article.getViews());
    }
}





