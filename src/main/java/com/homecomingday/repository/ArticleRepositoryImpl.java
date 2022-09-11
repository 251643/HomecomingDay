package com.homecomingday.repository;

import com.homecomingday.controller.response.MyPageDetailResponseDto;
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

    @Override
    public Slice<MyPageDetailResponseDto> getArticleScroll(Pageable pageable) {

        QueryResults<Article> result = queryFactory
                .selectFrom(article)
                //.where(article.articleFlag.eq(articleFlag))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.Id.desc())
                .fetchResults();

        List<MyPageDetailResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article articles : result.getResults()) {
            articleResponseDtoList.add( MyPageDetailResponseDto.builder()
                    .articleId(articles.getId())
                    .title(articles.getTitle())
                    .username(articles.getMember().getUsername())
                    .departmentName(articles.getMember().getDepartmentname())
                    .createdAt(Time.convertLocaldatetimeToTime(articles.getCreatedAt()))
                    .admission(articles.getMember().getAdmission().substring(2,4)+"학번")
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

    public String changearticleFlag(String articleFlag) {
        if (articleFlag.equals("help")) {
            return "도움요청";
        } else if(articleFlag.equals("freeTalk")){
            return "자유토크";
        }else if(articleFlag.equals("information")){
            return "정보공유";
        }else if(articleFlag.equals("calendar")){
            return "만남일정";
        }
        return null;
    }

}