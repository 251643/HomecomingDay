package com.homecomingday.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecomingday.controller.request.CommentRequestDto;
import com.homecomingday.controller.response.ReviseContentDto;
import com.homecomingday.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false)
  private String content;


  @Column(nullable = false)
  private String articleFlag;


  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member member;

  @JsonBackReference
  @JoinColumn(name = "article_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Article article;

  public Comment(Article article, ReviseContentDto reviseContentDto, UserDetailsImpl userDetails) {
    this.article = article;
    this.content = reviseContentDto.getContent();
    this.member = userDetails.getMember();
//    this.free = free.getArticlesId();
//    이메일이 들어올자리
  }



  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  public void updateComment(CommentRequestDto commentRequestDto) {
    this.content= commentRequestDto.getContent();
  }
}
