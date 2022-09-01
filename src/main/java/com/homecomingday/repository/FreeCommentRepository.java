package com.homecomingday.repository;

import com.homecomingday.domain.FreeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeCommentRepository extends JpaRepository<FreeComment,Long> {

    @Query("select p from FreeComment p ORDER BY p.createdAt DESC")
    List<FreeComment>findByFree_Id(Long Id);
}
