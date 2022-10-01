package com.homecomingday.article;

import com.homecomingday.article.responseDto.GetAllArticleDto;
import com.homecomingday.myPage.responseDto.MyPageDetailResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<MyPageDetailResponseDto> getArticleScroll2(Pageable pageable, UserDetailsImpl userDetails);
    Page<GetAllArticleDto> getReadAllArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag);

    Page<GetAllArticleDto> readPopularArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag);

    Page<GetAllArticleDto> searchPopularArticle(Pageable pageable,UserDetailsImpl userDetails);

    Page<GetAllArticleDto> searchAllArticle(Pageable pageable, UserDetailsImpl userDetails);

}