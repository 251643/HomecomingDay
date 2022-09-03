package com.homecomingday.repository;

import com.homecomingday.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select p from Comment p ORDER BY p.createdAt DESC")
    List<Comment>findByFree_Id(Long Id);
}
