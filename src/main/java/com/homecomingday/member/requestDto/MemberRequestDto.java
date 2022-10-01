package com.homecomingday.member.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Pattern(regexp = "^[가-힣a-zA-Z]{2,16}$")
  private String username;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,16}$")
  private String password;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,16}$")
  private String passwordCheck;

}
