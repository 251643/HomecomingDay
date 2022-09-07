package com.homecomingday.repository;

import com.homecomingday.controller.response.ArticleResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ArticleRepositoryCustom {
    Slice<ArticleResponseDto> getArticleScroll(Pageable pageable, String nickname);
}