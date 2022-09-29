package com.homecomingday.repository;

import com.homecomingday.controller.response.GetAllArticleDto;
import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ArticleRepositoryCustom {
    Page<MyPageDetailResponseDto> getArticleScroll2(Pageable pageable, UserDetailsImpl userDetails);
    Page<GetAllArticleDto> getReadAllArticle(Pageable pageable, UserDetailsImpl userDetails, String articleFlag);
}