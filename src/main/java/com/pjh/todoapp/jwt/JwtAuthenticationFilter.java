package com.pjh.todoapp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjh.todoapp.Entity.dto.web.LoginRequestDto;
import com.pjh.todoapp.Entity.user.UserRoleEnum;
import com.pjh.todoapp.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
        // webSecurityConfig -> loginProcessingUrl("/api/user/login")
    }
    // UsernamePasswordAuthenticationFilter -> log(topic = "로그인 및 jwt 생성")
    // attemptAuthentication(req, res) -> log("로그인 시도")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // HttpServletRequest -> 클라이언트에서 서버로 http요청을 캡슐화 URL, HTTP메서드, 요청 헤더, 쿼리 문자열, 세션정보, 요청 파라미터, 바디 등등
        // HttpServletResponse -> 서버에서 클라이언트로 http응답을 보냄, 필요한 메서드 및 속성을 보내고 응답 상태코드 200, 404등등을 보냄
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            // JSON의 오브젝트형태로 변환하기 위해 ObjectMapper().readValue()
            // request.getInputStream으로 주입 및 class를 만들고 JSON데이터를 오브젝트화함.
            // Object화했기에 class로 저장이 가능
            log.info("requestDto.getUsername() : "+requestDto.getUsername()+"|| requestDto.getPassword() : "+requestDto.getPassword());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
                    // UsernamePasswordAuthenticationToken이 생성 후, AuthenticationManager에 전달
                    // UserDetailsService를 통해 사용자 정보 로드 및 입력된 비밀번호, 저장된 비밀번호 해시 비교하여 인증
                    // spring security 내부 로직을 통해 db와 select하여 확인
                    // 결론. UsernamePasswordAuthenticationToken이 생성한 후, Spring Security에서 내부 로직을 통해 select
                    // successfulaAuthentication(req, res, chain, authResult)로
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
            // 왜 IOException를 잡아서 RuntimeException으로 던지지?
            // 공통된 부분은 부모가 Exception이라는 것밖에 없음 ->
            // 1. 예외 처리 전환 : IOException이 일어나서 throw new RuntimeException으로
            // 이는 호출자에게 예외를 처리하지 않도록 강요하지 않고 예외를 일반적으로 처리 가능(편한 처리를 위해)
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(token, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
