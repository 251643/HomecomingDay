package com.homecomingday.repository;

import com.homecomingday.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> , ArticleRepositoryCustom{

    @Modifying
    @Query("update Article p set p.views = p.views + 1 where p.Id = :id")
    void updateCount(Long id);

    List <Article> findByArticleFlagAndSchoolNameOrderByCreatedAtDesc(String articleFlag, String schoolName);


    List <Article> findBySchoolNameOrderByCreatedAtDesc(String schoolName);

    List<Article> findByArticleFlagAndSchoolNameOrderByViewsDesc(String articleFlag, String schoolName);
}
