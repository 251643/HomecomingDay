package com.homecomingday.repository;

import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.domain.Article;
import com.homecomingday.domain.UserDetailsImpl;
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

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<MyPageDetailResponseDto> getArticleScroll(Pageable pageable, UserDetailsImpl userDetails) {

        QueryResults<Article> result = queryFactory
                .selectFrom(article)
                .where(article.member.email.eq(userDetails.getMember().getEmail()),
                        article.schoolName.eq(userDetails.getMember().getSchoolName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.Id.desc())
                .fetchResults();

            List<MyPageDetailResponseDto> articleResponseDtoList = new ArrayList<>();

           
               for (Article articles : result.getResults()) {
                   articleResponseDtoList.add(
                           MyPageDetailResponseDto.builder()
                                   .articleId(articles.getId())
                                   .title(articles.getTitle())
                                   .username(articles.getMember().getUsername())
                                   .departmentName(articles.getMember().getDepartmentName())
                                   .createdAt(Time.convertLocaldatetimeToTime(articles.getCreatedAt()))
                                   .admission(articles.getMember().getAdmission().substring(2, 4) + "학번")
                                   .articleFlag(changearticleFlag(articles.getArticleFlag()))
                                   .views(articles.getViews())
                                   .commentCnt((long) articles.getComments().size()) // 0으로 기본세팅
                                   .build()
                   );
               }

               boolean hasNext = false;
               if (articleResponseDtoList.size() > pageable.getPageSize()) {
                   articleResponseDtoList.remove(pageable.getPageSize());
                   hasNext = true;
               }
               return new SliceImpl<>(articleResponseDtoList, pageable, hasNext);

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
                            .articleFlag(changearticleFlag(articles.getArticleFlag()))
                            .views(articles.getViews())
                            .commentCnt((long) articles.getComments().size()) // 0으로 기본세팅
                            .build()
            );
        }

        return new PageImpl<>(articleResponseDtoList, pageable, total.size());
    }

    public String changearticleFlag(String articleFlag) {
        if (articleFlag.equals("help")) {
            return "도움요청";
        } else if (articleFlag.equals("freeTalk")) {
            return "자유토크";
        } else if (articleFlag.equals("information")) {
            return "정보공유";
        } else if (articleFlag.equals("calendar")) {
            return "만남일정";
        }
        return null;
    }

}
