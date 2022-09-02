package com.homecomingday.domain;

import com.homecomingday.controller.response.CommentDto;
import com.homecomingday.controller.response.ReviseContentDto;
import com.homecomingday.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FreeComment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;


  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "free_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Free free;

  public FreeComment(Free free, ReviseContentDto reviseContentDto, UserDetailsImpl userDetails) {
    this.free = free;
    this.content = reviseContentDto.getContent();
    this.member = userDetails.getMember();
//    this.free = free.getArticlesId();
//    이메일이 들어올자리
  }



  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
