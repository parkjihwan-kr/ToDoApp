package com.pjh.todoapp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjh.todoapp.Entity.dto.CMRespDto;
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
        // login process -> /api/user/login ... controller를 접근을 하지 않고(filter역할) jwtAuthentioncationFilter로 사용해서 만듦.
        // 그럼으로 js또는 html에서 사용하는 login.html에서 ajax 비동기 통신에서 method : "post", url : `/api/user/login`
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // HttpServletRequest -> 클라이언트에서 서버로 http요청을 캡슐화 URL, HTTP메서드, 요청 헤더, 쿼리 문자열, 세션정보, 요청 파라미터, 바디 등등
        // HttpServletResponse -> 서버에서 클라이언트로 http응답을 보냄, 필요한 메서드 및 속성을 보내고 응답 상태코드 200, 404등등을 보냄
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            // 직접 LoginRequestDto를 json오브젝트로 읽어봐야함. controller에서는 @RequestBody, @RestController와 같은 json변환형식의 데이터 처리가 가능하지만
            // 여기는 controller 앞단에 존재하는 filter이기에 @RequestBody를 사용해서 읽질 못함
            // JSON의 오브젝트형태로 변환하기 위해 ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class)
            // LoginRequestDto의 json형태-> 객체(class)를 하기 위해 request.getInputStream(), loginRequestDto.class 파라미터 사용
            // 객체로 변환되었기에 새로 생성한 인스턴스에 넣어줄 수 있음.

            log.info("requestDto.getUsername() : "+requestDto.getUsername()+" || requestDto.getPassword() : "+requestDto.getPassword());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
                    // SecurityContextHolder 내부에 존재하는 Authentication은 principal, credentials, authorities를 가짐
                    // return 값에 Authentication을 반환하기 위해서 getAuthenticationManager()객체를 불러서
                    // getAuthenticationManager.authenticate(); -> Authentication
                    // 내부 정보를 넣어주기 위해 Authentication의 구현체인 UsernamePasswordAuthenticationToken을 사용
                    // usernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword(), null);을 넣음
                    // UsernamePasswordAuthenticationToken은 주로 유저네임과 패스워드를 넣는 저장소임.
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
        // Authentication 객체는 spring security 내부 객체이며 user, user.getUsername()와 user.getPassword() 같은 정보를 가짐(UserDetailsImpl)참고
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        // LoginRequestDto의 필드는 String username, String password인데 UserDetailsImpl에서 getPrincipal().getUser() -> Role타입을 가지고 있음

        // 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고,
        // 발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
        String token = jwtUtil.createToken(username, role);
        // 토큰의 파라미터로 username(principal().getUser() -> 인증된 유저), role -> (authResult.getPrincipal().getUser().getRole() 인증된 유저의 RoleType : ADMIN || USER)
        jwtUtil.addJwtToCookie(token, response);
        // 로그인에 성공했으니 jwt Token을 발급해줌. 인증, 보안, 권한 부여 등등,,,, -> 이를 또 쿠키로
        // 즉 TOKEN발급, jwt를 cookie속에 넣어줌
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
