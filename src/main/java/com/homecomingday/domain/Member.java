package com.homecomingday.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecomingday.member.requestDto.SchoolInfoDto;
import com.homecomingday.myPage.responseDto.MyPageResponseDto;
import com.homecomingday.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column
  private String admission;

  @Column
  private String schoolName;

  @Column
  private String departmentName;

  @Column
  private String userImage;

  @Column
  private String salt;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Notification> notification = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Member member = (Member) o;
    return id != null && Objects.equals(id, member.id);
  }
  public void update(SchoolInfoDto schoolInfoDto) {
    this.schoolName = schoolInfoDto.getSchoolName();
    this.departmentName = schoolInfoDto.getDepartmentName();
    this.admission = schoolInfoDto.getAdmission();
  }
  @Override
  public int hashCode() {
    return getClass().hashCode();
  }



  public void updateMyPage(MyPageResponseDto myPageResponseDto){
    this.userImage = myPageResponseDto.getUserImage();
  }
}
