package com.homecomingday.service;

import com.homecomingday.controller.TokenDto;
import com.homecomingday.controller.response.NaverMemberInfoDto;
import com.homecomingday.domain.Member;
import com.homecomingday.domain.RefreshToken;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.jwt.TokenProvider;
import com.homecomingday.repository.MemberRepository;
import com.homecomingday.repository.RefreshTokenRepository;
import com.homecomingday.shared.NaverLogin;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NaverUserInfoService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    public TokenDto naverUserInfo(String AccessToken,  HttpServletResponse response) throws ParseException {
        String token = AccessToken; // 네이버 로그인 접근 토큰;
        if(token == null){ // && token.equals("")){
            throw new RuntimeException("AccessToken값이 없습니다.");
        }
        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", token);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(responseBody);
        JSONObject jsonObj = (JSONObject) obj;

        JSONObject response_obj = (JSONObject)jsonObj.get("response");
        String name = (String)response_obj.get("name");
        String email = (String)response_obj.get("email");

        NaverMemberInfoDto naverMemberInfoDto = new NaverMemberInfoDto(email, name);

        Member member = registerNaver(naverMemberInfoDto);
        forceLogin(member);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);
        return tokenDto;
    }

    private  String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
                System.out.println(">>>>" + line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
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
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
        response.addHeader("Username", tokenDto.getUsername());
        response.addHeader("SchoolInfo", String.valueOf(tokenDto.isSchoolInfo()));
        response.addHeader("SchoolName", String.valueOf(tokenDto.getSchoolName()));
    }
}