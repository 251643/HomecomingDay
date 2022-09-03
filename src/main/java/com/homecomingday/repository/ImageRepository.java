package com.homecomingday.repository;


import com.homecomingday.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
   // List<Image> findAllByBoardName(Long articleId);
}
