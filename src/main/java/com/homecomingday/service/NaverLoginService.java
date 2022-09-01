package com.homecomingday.service;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.homecomingday.controller.TokenDto;
import com.homecomingday.controller.response.MemberResponseDto;
import com.homecomingday.controller.response.NaverMemberInfoDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.RefreshToken;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.MemberRepository;
import com.homecomingday.repository.RefreshTokenRepository;
import com.homecomingday.shared.Authority;
import com.homecomingday.shared.NaverLogin;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NaverLoginService {

    private final NaverLogin naverLogin;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private String apiResult = null;

    public void naverLogin(Model model, HttpSession session) {

        /* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
        String naverAuthUrl = naverLogin.getAuthorizationUrl(session);
        System.out.println("네이버:" + naverAuthUrl);

        model.addAttribute("url", naverAuthUrl);
    }

    public TokenDto naverLoginCallback(Model model, String code, String state, HttpSession session, HttpServletResponse response) throws IOException, ParseException {
        OAuth2AccessToken oauthToken;
        oauthToken = naverLogin.getAccessToken(session, code, state);

        //1. 로그인 사용자 정보를 읽어온다.
        apiResult = naverLogin.getUserProfile(oauthToken);

        //String형식의 json데이터
        /** apiResult json 구조
         {"resultcode":"00",
         "message":"success",
         "response":{"id":"33666449","nickname":"shinn****","age":"20-29","gender":"M","email":"sh@naver.com","name":"\uc2e0\ubc94\ud638"}}		**/

        //2. String형식인 apiResult를 json형태로 바꿈
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(apiResult);
        JSONObject jsonObj = (JSONObject) obj;

        //3. 데이터 파싱
        // Top레벨 단계 _response 파싱
        JSONObject response_obj = (JSONObject)jsonObj.get("response");
        //response의 nickname값 파싱
        String name = (String)response_obj.get("name");
        String email = (String)response_obj.get("email");
//
//        //4.파싱 닉네임 세션으로 저장
//        session.setAttribute("sessionId",name);
//        //세션 생성
//        model.addAttribute("result", apiResult);
        NaverMemberInfoDto naverMemberInfoDto = new NaverMemberInfoDto(email, name);

        Member member = registerNaver(naverMemberInfoDto);
        forceLogin(member);

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .token(oauthToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshTokenObject);
        TokenDto tokenDto = TokenDto.builder()
                //  .grantType(oauthToken.getTokenType())
                .accessToken(oauthToken.getAccessToken())
                .accessTokenExpiresIn(Long.valueOf(oauthToken.getExpiresIn()))
                .refreshToken(oauthToken.getRefreshToken())
                .build();

        tokenToHeaders(tokenDto, response);

        return tokenDto;
    }

    private Member registerNaver(NaverMemberInfoDto naverMemberInfoDto) {

        Member naverMember = memberRepository.findByEmail(naverMemberInfoDto.getEmail())
                .orElse(null);
        if (naverMember == null) {
            // 회원가입
            String email = naverMemberInfoDto.getEmail();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            String name = naverMemberInfoDto.getName();

            Member member = Member.builder()
                    .email(email)
                    .username(name)
                    .password(encodedPassword)
                    .build();
            memberRepository.save(member);

            return member;
        }
        return naverMember;
    }


    private void forceLogin(Member member) {
        UserDetails userDetails = new UserDetailsImpl(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.addHeader("Authorization", tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
        response.addHeader("Username", tokenDto.getUsername());
    }

}
