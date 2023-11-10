package com.pjh.todoapp.jwt;

import com.pjh.todoapp.Entity.user.UserRoleEnum;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // util class 특정 파라미터에 대한 작업을 시작
    // 하나의 모듈로써 사용하는 클래스
    // ex) 문자열을 조작하는 유틸 클래스, 날짜열을 조작하는 클래스 묶음-> 모듈
    // 이는 jwt을 조작하는 유틸 클래스

    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Header KEY 값 = 쿠키의 NAME 값

    public static final String AUTHORIZATION_KEY = "auth";
    // 사용자 권한 값의 KEY

    public static final String BEARER_PREFIX = "Bearer ";
    // Token 식별자

    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    // 토큰 만료시간

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        logger.info("init start");
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        logger.info("createToken");
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)               // 사용자 식별자값(ID), 나중에 pK로 식별자값 넣기
                        .claim(AUTHORIZATION_KEY, role)     // 사용자 권한(선택사항)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date)                  // 발급일(선택사항)
                        .signWith(key, signatureAlgorithm)  // 암호화 알고리즘
                        .compact();
    }
    // JWT 생성 메서드(cookie 객체 생성 또는 reponseHeader에서 내보는 방법)

    public void addJwtToCookie(String token, HttpServletResponse res) {
        // 쿠키는 공백 불가
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");
            logger.info("cookie 추가");
            // Response 객체에 Cookie 추가
            res.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    // 생성된 JWT를 cookie에 저장

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            logger.info("substringToken");
            // 공백과 null이 되면 안되고 Bearer로 시작하는지
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }
    // cookie에 들어있던 JWT 토큰을 substring

    public boolean validateToken(String token) {
        try {
            logger.info("validateToken");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    // JWT 검증

    public Claims getUserInfoFromToken(String token) {
        logger.info("getUserInfoFromToken");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // JWT 사용자 정보를 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        logger.info("getTokenFromRequest");
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
}

// jwt 데이터 (secret Key)

// JWT 생성 메서드(cookie 객체 생성 또는 reponseHeader에서 내보는 방법)
// 서버에서 cookie를 만들면 ... 쿠키 만료기간 설정, setCookie활용 가능

// 생성된 JWT를 cookie에 저장

// cookie에 들어있던 JWT 토큰을 substring

// JWT 검증

// JWT 사용자 정보를 가져오기