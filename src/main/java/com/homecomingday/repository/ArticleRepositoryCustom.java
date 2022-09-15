package com.homecomingday.repository;

import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ArticleRepositoryCustom {
    Slice<MyPageDetailResponseDto> getArticleScroll(Pageable pageable, UserDetailsImpl userDetails);
}