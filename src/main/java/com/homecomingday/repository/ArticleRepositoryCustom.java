package com.homecomingday.repository;

import com.homecomingday.controller.response.GetAllArticleDto;
import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.domain.Article;
import com.homecomingday.domain.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArticleRepositoryCustom {
    Page<MyPageDetailResponseDto> getArticleScroll2(Pageable pageable, UserDetailsImpl userDetails);
    Page<GetAllArticleDto> getReadAllArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag);

    Page<GetAllArticleDto> readPopularArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag);

    Page<GetAllArticleDto> searchPopularArticle(Pageable pageable,UserDetailsImpl userDetails);

    Page<GetAllArticleDto> searchAllArticle(Pageable pageable, UserDetailsImpl userDetails);

}