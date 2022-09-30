package com.homecomingday.domain;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="article_id",nullable = false)
    private Article article;

    public Participant(Member member, Article article) {
        this.member=member;
        this.article=article;
    }
}
