package com.homecomingday.repository;

import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.domain.Article;
import com.homecomingday.util.Time;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.homecomingday.domain.QArticle.*;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public String CreatedAtCustom(Timestamp timestamp) {
        String timestampToString = timestamp.toString();
//        String timestampToString = "2022-08-21T14:54:46.247+00:00";
        String customTimestamp = timestampToString.substring(5, 10);
        customTimestamp = customTimestamp.replace("-", "월 ");
        if (customTimestamp.startsWith("0")) {
            customTimestamp = customTimestamp.substring(1);
        }
        System.out.println(customTimestamp);
        return customTimestamp;
    }

    public Slice<ArticleResponseDto> getArticleScroll(Pageable pageable) {

        QueryResults<Article> result = queryFactory
                .selectFrom(article)
                //.where(article.articleFlag.eq(articleFlag))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.Id.desc())
                .fetchResults();

        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        for (Article eachArticle : result.getResults()) {
            articleResponseDtoList.add(ArticleResponseDto.builder()
                    .articleId(eachArticle.getId())
                    .title(eachArticle.getTitle())
                    .username(eachArticle.getMember().getUsername())
                    .createdAt(Time.convertLocaldatetimeToTime(eachArticle.getCreatedAt()))
                    .admission(eachArticle.getMember().getAdmission().substring(2,4)+"학번")
                    .views(eachArticle.getViews())
                    .commentCnt(0L)
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
}