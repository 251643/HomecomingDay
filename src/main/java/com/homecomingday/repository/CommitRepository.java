package com.homecomingday.repository;

import com.homecomingday.domain.Article;
import com.homecomingday.domain.Comment;
import com.homecomingday.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitRepository  extends JpaRepository<Commit,Long> {

    List<Commit> findByCommentAndArticle(Comment comment, Article findArticle);
}
