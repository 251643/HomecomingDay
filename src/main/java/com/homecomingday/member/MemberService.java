package com.homecomingday.member;

import com.homecomingday.dto.TokenDto;
import com.homecomingday.member.requestDto.EmailRequestDto;
import com.homecomingday.member.requestDto.LoginRequestDto;
import com.homecomingday.member.requestDto.MemberRequestDto;
import com.homecomingday.member.requestDto.SchoolInfoDto;
import com.homecomingday.member.responseDto.MemberResponseDto;
import com.homecomingday.dto.ResponseDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.jwt.JwtDecoder;
import com.homecomingday.jwt.TokenProvider;
import com.homecomingday.member.MemberRepository;
import com.homecomingday.util.Encrypt;
import com.homecomingday.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;
  private final JwtDecoder jwtDecoder;
  private final RedisUtil redisUtil;


  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto) {
    if (null != isPresentMember(requestDto.getEmail())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
          "중복된 닉네임 입니다.");
    }
    String salt = Encrypt.getSalt();
    Member member = Member.builder()
            .email(requestDto.getEmail())
            .username(requestDto.getUsername())
                .password(Encrypt.getEncrypt(requestDto.getPassword(), salt))
            .salt(salt)
                    .build();
    memberRepository.save(member);
    return ResponseDto.success(
        MemberResponseDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .username(member.getUsername())
            .createdAt(member.getCreatedAt())
            .modifiedAt(member.getModifiedAt())
            .build()
    );
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
    Member member = isPresentMember(requestDto.getEmail());
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    if (!validatePassword(requestDto.getEmail(), requestDto.getPassword())) {
      return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
    }

    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
    tokenToHeaders(tokenDto, response);

    return ResponseDto.success(tokenDto);

  }

  private boolean validatePassword(String email, String password) {

    Member member = isPresentMember(email);
    return member.getPassword().equals(Encrypt.getEncrypt(password, member.getSalt()));
  }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "사용자를 찾을 수 없습니다.");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }

  public ResponseDto<?> logout(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    Member member = tokenProvider.getMemberFromAuthentication();
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    return tokenProvider.deleteRefreshToken(member);
  }

  @Transactional(readOnly = true)

  public Member isPresentMember(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);

    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    response.addHeader("Username", tokenDto.getUsername());
    response.addHeader("SchoolInfo", String.valueOf(tokenDto.isSchoolInfo()));
    response.addHeader("SchoolName", String.valueOf(tokenDto.getSchoolName()));
  }

  @Transactional
  public ResponseDto<?> schoolInfoMember(SchoolInfoDto requestDto, UserDetailsImpl userDetails, HttpServletResponse response) {
    Member signupMember = memberRepository.findByEmail(userDetails.getUsername()).orElse(null);
    schoolNameToHeaders(requestDto, response);
    signupMember.update(requestDto);
    return ResponseDto.success(
            MemberResponseDto.builder()
                    .id(signupMember.getId())
                    .email(signupMember.getEmail())
                    .username(signupMember.getUsername())
                    .schoolname(signupMember.getSchoolName())
                    .departmentname(signupMember.getDepartmentName())
                    .admission(signupMember.getAdmission())
                    .createdAt(signupMember.getCreatedAt())
                    .modifiedAt(signupMember.getModifiedAt())
                    .build()
    );
  }
  public void schoolNameToHeaders(SchoolInfoDto requestDto, HttpServletResponse response) {
    response.addHeader("SchoolName", String.valueOf(requestDto.getSchoolName()));
  }

  public ResponseDto<?> checkEmail(EmailRequestDto.EmailSendRequestDto emailSendRequestDto) {

    if (null != isPresentMember(emailSendRequestDto.getEmail())) {
      return ResponseDto.fail("DUPLICATED_EMAIL",
              "동일한 이메일이 존재합니다.");
    }
    return ResponseDto.success(emailSendRequestDto.getEmail());
  }

  public ResponseDto<?> updateAccessToken(String refreshToken){
    //리프레시토큰 만료시간이 지나지 않았을 경우

    if (!tokenProvider.validateToken(refreshToken)) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    Member member = tokenProvider.getMemberFromAuthentication();
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "사용자를 찾을 수 없습니다.");
    }

      UserDetailsImpl userDetails = new UserDetailsImpl(member);

      //액세스 토큰 생성
      final String token = TokenProvider.generateAccessToken(userDetails);
      System.out.println("새로운 액세스 토큰: "+token);

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization","Bearer "+token);

      return ResponseDto.success(TokenDto.builder()
                      .grantType("Bearer")
                      .accessToken(token)
                      //.accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                      .refreshToken(refreshToken)
                      .username(userDetails.getMember().getUsername())
                      .schoolInfo(StringUtils.isNotBlank(userDetails.getMember().getSchoolName()))
                      .schoolName(userDetails.getMember().getSchoolName())
                      .build());

  }
}
