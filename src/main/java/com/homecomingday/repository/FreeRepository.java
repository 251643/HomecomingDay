package com.homecomingday.repository;

import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.domain.Free;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeRepository extends JpaRepository<Free, Long> {


    List <ArticleResponseDto> findAllByOrderByCreatedAtDesc();
}
