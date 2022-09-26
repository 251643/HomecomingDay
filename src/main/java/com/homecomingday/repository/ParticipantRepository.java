package com.homecomingday.repository;

import com.homecomingday.domain.Article;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {


    List<Participant> findAllByMember(Member member);

    Participant findByMemberAndArticle(Member member, Article article);
}
