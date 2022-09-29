package com.homecomingday.repository;

import com.homecomingday.controller.response.*;
import com.homecomingday.domain.*;

import com.homecomingday.util.ArticleChange;
import com.homecomingday.util.Time;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.homecomingday.domain.QArticle.*;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final CommitRepository commitRepository;
    private final ImageRepository imageRepository;
    private final HeartRepository heartRepository;

    public ArticleRepositoryImpl(EntityManager em, CommitRepository commitRepository, ImageRepository imageRepository, HeartRepository heartRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.commitRepository = commitRepository;
        this.imageRepository = imageRepository;
        this.heartRepository = heartRepository;
    }

    @Override
    public Page<MyPageDetailResponseDto> getArticleScroll2(Pageable pageable, UserDetailsImpl userDetails) {
        List<Article> result = queryFactory
                .selectFrom(article)
                .where(article.member.email.eq(userDetails.getMember().getEmail()),
                        article.schoolName.eq(userDetails.getMember().getSchoolName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.Id.desc())
                .fetch();

        List<Article> total = queryFactory
                .selectFrom(article)
                .where(article.member.email.eq(userDetails.getMember().getEmail()),
                        article.schoolName.eq(userDetails.getMember().getSchoolName()))
                .orderBy(article.Id.desc())
                .fetch();

        List<MyPageDetailResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article articles : result) {
            articleResponseDtoList.add(
                    MyPageDetailResponseDto.builder()
                            .articleId(articles.getId())
                            .title(articles.getTitle())
                            .username(articles.getMember().getUsername())
                            .departmentName(articles.getMember().getDepartmentName())
                            .createdAt(Time.convertLocaldatetimeToTime(articles.getCreatedAt()))
                            .admission(articles.getMember().getAdmission().substring(2, 4) + "학번")
                            .articleFlag(ArticleChange.changearticleFlag(articles.getArticleFlag()))
                            .views(articles.getViews())
                            .heartCnt((long) articles.getHeartList().size())
                            .commentCnt((long) articles.getComments().size()) // 0으로 기본세팅
                            .build()
            );
        }

        return new PageImpl<>(articleResponseDtoList, pageable, total.size());
    }


    @Override
    public Page<GetAllArticleDto> getReadAllArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag) {
        List<Article> result = queryFactory
                .selectFrom(article)
                .where(article.schoolName.eq(userDetails.getMember().getSchoolName()),
                        article.articleFlag.eq(articleFlag))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.createdAt.desc())
                .fetch();

        List<Article> total = queryFactory
                .selectFrom(article)
                .where(article.schoolName.eq(userDetails.getMember().getSchoolName()),
                        article.articleFlag.eq(articleFlag))
                .orderBy(article.createdAt.desc())
                .fetch();

        List<GetAllArticleDto> articleResponseDtoList = new ArrayList<>();

        for (Article findArticle : result) {
            List<Comment> commentList = findArticle.getComments(); //게시물 index 번호에 따라 뽑아옴
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();   //for문 안에 있어야 계속 초기화돼서 들어감
            for (Comment comment : commentList) {
                List<Commit> commitList = commitRepository.findByCommentAndArticle(comment,findArticle);
                List<CommitResponseDto> commitResponseDtoList = new ArrayList<>();
                for (Commit commit : commitList) {
                    commitResponseDtoList.add(
                            CommitResponseDto.builder()
                                    .childCommentId(commit.getId())
                                    .content(commit.getContent())
                                    .username(commit.getMember().getUsername())
                                    .userImage(ArticleChange.changeImage(commit.getMember().getUserImage()))
                                    .admission(commit.getMember().getAdmission().substring(2, 4) + "학번")
                                    .departmentName(commit.getMember().getDepartmentName())
                                    .createdAt(Time.convertLocaldatetimeToTime(commit.getCreatedAt()))
                                    .build()
                    );
                }

                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .username(comment.getMember().getUsername())
                                .userImage(ArticleChange.changeImage(comment.getMember().getUserImage()))
                                .admission(comment.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(comment.getMember().getDepartmentName())
                                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                                .childCommentList(commitResponseDtoList)
                                .build()

                );
            }

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

                articleResponseDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .imageList(pickImage)
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .userImage(ArticleChange.changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                .views(findArticle.getViews())
                                .heartCnt(findArticle.getHeartCnt())
                                .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                .commentCnt((long) commentResponseDtoList.size())
                                .commentList(commentResponseDtoList)
                                .build()
                );
            } else { //만남일정 부분  출력
                articleResponseDtoList.add(
                        GetAllArticleDto.builder()
                                .articleId(findArticle.getId())
                                .title(findArticle.getTitle())
                                .content(findArticle.getContent())
                                .calendarDate(ArticleChange.changeCalendarDate(findArticle.getCalendarDate()))
                                .calendarTime(findArticle.getCalendarTime())
                                .calendarLocation(findArticle.getCalendarLocation())
                                .maxPeople(findArticle.getMaxPeople())
                                .username(findArticle.getMember().getUsername())
                                .userId(findArticle.getMember().getId())
                                .userImage(ArticleChange.changeImage(findArticle.getMember().getUserImage()))
                                .createdAt(Time.convertLocaldatetimeToTime(findArticle.getCreatedAt()))
                                .admission(findArticle.getMember().getAdmission().substring(2, 4) + "학번")
                                .departmentName(findArticle.getMember().getDepartmentName())
                                .articleFlag(ArticleChange.changearticleFlag(articleFlag))
                                .views(findArticle.getViews())
                                .heartCnt(findArticle.getHeartCnt())
                                .isHeart(heartCheck(findArticle, userDetails.getMember()))
                                .commentCnt((long) commentResponseDtoList.size())
                                .commentList(commentResponseDtoList)
                                .build()
                );
            }
        }

        return new PageImpl<>(articleResponseDtoList, pageable, total.size());
    }

    public boolean heartCheck(Article article, Member member) {
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return heartRepository.existsByMemberAndArticle(member, article);

    }

}
