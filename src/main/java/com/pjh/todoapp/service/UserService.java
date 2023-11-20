package com.pjh.todoapp.service;

import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.web.SignupRequestDto;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Entity.user.UserRoleEnum;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.UserRepository;
import com.pjh.todoapp.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // 관리자 권한 부여, 다른 방식으로 관리자 권한을 부여할 예정

    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        Optional<User> checkUsername = userRepository.findByUsername(username);
        // email과 username은 중복되면 안됨
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        String email = signupRequestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
        long boardUserId = userRepository.findById(user.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다."))
                .getId();
        String boardTitle = "test";
        String boardContents = "test";

        Board board = new Board(boardTitle,boardContents);
        board.setUser(user);
        // userId를 넣기 위한 작업
        boardRepository.save(board);
    }

}
