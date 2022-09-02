package com.homecomingday.repository;

import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.domain.Free;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeRepository extends JpaRepository<Free, Long> {

    @Modifying
    @Query("update Free p set p.views = p.views + 1 where p.id = :id")
    int updateCount(Long id);
    List <ArticleResponseDto> findAllByOrderByCreatedAtDesc();
}
