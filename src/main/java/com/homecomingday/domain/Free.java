package com.homecomingday.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecomingday.controller.response.CommentDto;
import com.homecomingday.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Free extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;


  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FreeComment> freeComments;

//  @Transient //사진은 보여지기만 하면 되므로 불필요하게 관게를 맺기보단 일시적으로 체류만 시켜주면됨
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
  private final List<Image> imageList = new ArrayList<>();



  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;





//  public FreeComment(CommentDto commentDto) {
//    this.comment = commentDto.getComment();
//  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

}
