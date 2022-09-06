package com.homecomingday.repository;


import com.homecomingday.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findAllById(Long articleId);
//    List<Image> findbyArticle_Id(Long articleId);
    // List<Image> findAllByBoardName(Long articleId);
}
