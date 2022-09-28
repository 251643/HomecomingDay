package com.homecomingday.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.homecomingday.jwt.TokenProvider.*;
@Component
@RequiredArgsConstructor
public class JwtDecoder {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    byte[] keyBytes = Decoders.BASE64.decode("dkqkRnjdiehlsmsepdlrjsdfsdfsdafdsdksqkRnawlsWkwhffkrprFSDFSDFnlcksgdmawlsWKWkwmdsksekdmfklsdfjkldsjfsdmfklsdfsdlhfksdjfksdfjSDFSADFSDdklsfjklsad");
    Key key = Keys.hmacShaKeyFor(keyBytes);
    public String decodeUsername(String token) {

        String username = "";
        try {
            username = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

            //Date expiration = claims.get("exp", Date.class);
            //String data = claims.get("data", String.class);
            System.out.println("username>>>>>>>>>>>>>>>>>>>>>>>"+username);
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            System.out.println(e);
        } catch (Exception e) { // 그외 에러났을 경우
            System.out.println(e);
        }
        return username;
    }

}