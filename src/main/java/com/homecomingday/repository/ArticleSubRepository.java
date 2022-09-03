package com.homecomingday.repository;

import com.homecomingday.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleSubRepository extends JpaRepository<Article, Long> {
}
