package com.homecomingday.domain;

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

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "free_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Free free;

  @Column(nullable = false)
  private String content;


  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
