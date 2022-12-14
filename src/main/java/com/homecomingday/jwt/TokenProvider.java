package com.homecomingday.jwt;

import com.homecomingday.dto.TokenDto;
import com.homecomingday.dto.ResponseDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.RefreshToken;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.member.RefreshTokenRepository;
import com.homecomingday.shared.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.utils.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@Configuration
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30 * 2 ;            //30분
  private static final long REFRESH_TOKEN_EXPRIRE_TIME = 1000 * 60 * 60 * 24 * 7;     //7일
  private static Key key;


  private final RefreshTokenRepository refreshTokenRepository;

  public TokenProvider(@Value("${jwt.secret}") String secretKey,
                       RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto generateTokenDto(Member member) {
    long now = (new Date().getTime());

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
            .setSubject(member.getEmail())
            .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString())
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPRIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    RefreshToken refreshTokenObject = RefreshToken.builder()
            .id(member.getId())
            .member(member)
            .token(refreshToken)
            .build();

    refreshTokenRepository.save(refreshTokenObject);
    return TokenDto.builder()
            .grantType(BEARER_PREFIX)
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
            .refreshToken(refreshToken)
            .username(member.getUsername())
            .schoolInfo(StringUtils.isNotBlank(member.getSchoolName()))
            .schoolName(member.getSchoolName())
            .build();

  }

//  public Authentication getAuthentication(String accessToken) {
//    Claims claims = parseClaims(accessToken);
//
//    if (claims.get(AUTHORITIES_KEY) == null) {
//      throw new RuntimeException("권한 정보가 없는 토큰 입니다.");
//    }
//
//    Collection<? extends GrantedAuthority> authorities =
//        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//            .map(SimpleGrantedAuthority::new)
//            .collect(Collectors.toList());
//
//    UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
//
//    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//  }

  public Member getMemberFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || AnonymousAuthenticationToken.class.
            isAssignableFrom(authentication.getClass())) {
      return null;
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

//  private Claims parseClaims(String accessToken) {
//    try {
//      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//    } catch (ExpiredJwtException e) {
//      return e.getClaims();
//    }
//  }

  @Transactional(readOnly = true)
  public RefreshToken isPresentRefreshToken(Member member) {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
    return optionalRefreshToken.orElse(null);
  }

  @Transactional
  public ResponseDto<?> deleteRefreshToken(Member member) {
    RefreshToken rTokenEntity = isPresentRefreshToken(member);
    if (null == rTokenEntity) {
      return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
    }

    refreshTokenRepository.delete(rTokenEntity);
    return ResponseDto.success("success");
  }
//  public String validateRefreshToken(RefreshToken refreshTokenObj){
//    // refresh 객체에서 refreshToken 추출
//    String refreshToken = refreshTokenObj.getToken();
//    try {
//      // 검증
//      Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
//
//      //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
//      if (!claims.getBody().getExpiration().before(new Date())) {
//        return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
//      }
//    }catch (Exception e) {
//      //refresh 토큰이 만료되었을 경우, 로그인이 필요합니다.
//      return null;
//
//    }
//    return null;
//  }
//  public String recreationAccessToken(String userEmail, Object roles){
//
//    Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
//    claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
//    Date now = new Date();
//
//    //Access Token
//    String accessToken = Jwts.builder()
//            .setClaims(claims) // 정보 저장
//            .setIssuedAt(now) // 토큰 발행 시간 정보
//            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // set Expire Time
//            .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
//            // signature 에 들어갈 secret값 세팅
//            .compact();
//
//    return accessToken;
//  }

  //액세스토큰 생성
  public static String generateAccessToken(UserDetailsImpl userDetails) {
    String accessToken = null;
    try {
      long now = (new Date().getTime());
      Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
      accessToken = Jwts.builder()
              .setSubject(userDetails.getMember().getEmail())
              .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString())
              .setExpiration(accessTokenExpiresIn)
              .signWith(key, SignatureAlgorithm.HS256)
              .compact();

    } catch (Exception e) {

    }
    System.out.println(accessToken);
    return accessToken;
  }
}
