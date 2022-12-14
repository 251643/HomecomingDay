package com.homecomingday.article;

import com.homecomingday.domain.Article;
import com.homecomingday.domain.Heart;
import com.homecomingday.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long>{

        Heart findByMemberAndArticle(Member member, Article article);
        Boolean existsByMemberAndArticle(Member member, Article article);
}
