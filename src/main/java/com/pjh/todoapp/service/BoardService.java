package com.pjh.todoapp.service;


import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.UserRepository;
import com.pjh.todoapp.controller.dto.BoardResponseDto;
import com.pjh.todoapp.controller.dto.ResponseTodoListDto;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.util.ApiRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<ResponseTodoListDto> selectUserToDoList() {
        List<ResponseTodoListDto> userDtoList = new ArrayList<>();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            List<Board> myUserList = boardRepository.findByUser(user);

            // UserResponseDto에 데이터 추가
            ResponseTodoListDto userResponseDto = new ResponseTodoListDto();
            userResponseDto.setUsername(user.getUsername());
            userResponseDto.setUserId(user.getId());

            List<BoardResponseDto> boardDtoList = new ArrayList<>();
            for (Board board : myUserList) {
                // BoardDto에 데이터 추가
                BoardResponseDto boardDto = new BoardResponseDto();
                boardDto.setId(board.getId());
                boardDto.setTitle(board.getTitle());
                boardDto.setContents(board.getContents());
                boardDto.setCreatedAt(board.getCreatedAt());
                // boardDto.setCheckState(board.get);
                // 해당 값은 default값으로 false 설정
                boardDtoList.add(boardDto);
            }

            userResponseDto.setBoardDtoList(boardDtoList);
            userDtoList.add(userResponseDto);
        }
        return userDtoList;
    }
    @Transactional
    public Board addCard(Board board, UserDetailsImpl userDetails){
        board.setUser(userDetails.getUser());
        return boardRepository.save(board);
    }
    public Board selectPost(long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public Board updatePost(long id, Board user){

        Board userEntity = boardRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        userEntity.setTitle(user.getTitle());
        userEntity.setContents(user.getContents());
        //user.setCreatedDate(user.getCreatedDate());
        boardRepository.save(userEntity);
        return userEntity;
    }
    @Transactional
    public void deletePost(long boardId) {
        boardRepository.deleteById(boardId);
    }
}

