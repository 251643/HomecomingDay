package com.homecomingday.controller.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
  private Long id;
  private String email;
  private String username;
  private String schoolname;
  private String departmentname;
  private String admission;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
