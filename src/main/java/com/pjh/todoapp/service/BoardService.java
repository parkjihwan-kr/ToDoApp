package com.pjh.todoapp.service;


import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.UserRepository;
import com.pjh.todoapp.controller.dto.ResponseUserToDoListDto;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.security.UserDetailsServiceImpl;
import com.pjh.todoapp.util.ApiRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    @Transactional
    public Board 카드추가(Board board, UserDetailsImpl userDetails){
        // userDetails에 존재하는 것은 로그인한 사용자이고
        long id = userDetails.getUser().getId();
        System.out.println("id : "+id);
        return null;
    }
    @Transactional
    public List<ResponseUserToDoListDto> 모든유저투두카드조회() {
        List<ResponseUserToDoListDto> todoUserList = new ArrayList<>();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            List<Board> myUserList = boardRepository.findByUser(user);
            ResponseUserToDoListDto responseUserToDoListDto = new ResponseUserToDoListDto(user.getUsername(), myUserList);
            todoUserList.add(responseUserToDoListDto);
        }
        return todoUserList;
    }

    //for (Board board : boards) {
    //String username = board.getUsername();
    //String title = board.getTitle();

    //todoUserList.computeIfAbsent(username, key -> new ArrayList<>()).add(title);
    //}

    // userTitles 맵의 모든 필드를 출력
        /*for (Map.Entry<String, List<String>> entry : userTitles.entrySet()) {
            String username = entry.getKey();
            List<String> titles = entry.getValue();

            System.out.println("Username: " + username);
            System.out.println("Titles: " + titles);
        }*/

    @Transactional
    public Board 게시글조회(long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public Board 게시글수정(long id, Board user){

        Board userEntity = boardRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        //System.out.println("userEntity.getPassword()"+userEntity.getPassword()+" user.getPassword() : "+user.getPassword());

        /*if (!userEntity.getPassword().equals(user.getPassword())) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }*/

        userEntity.setTitle(user.getTitle());
        userEntity.setContents(user.getContents());
        //user.setCreatedDate(user.getCreatedDate());
        boardRepository.save(userEntity);
        return userEntity;
    }
    @Transactional
    public void 게시글삭제(Board user, long id) {
        Optional<Board> userOptional = boardRepository.findById(id);

        if (userOptional.isPresent()) {
            Board userInDatabase = userOptional.get();
            //String userPassword = userInDatabase.getPassword();

            /*if (userPassword.equals(user.getPassword())) {
                boardRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }*/
        } else {
            throw new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id);
        }
    }
}

