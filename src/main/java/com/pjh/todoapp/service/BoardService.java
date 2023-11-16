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
    @Transactional
    public List<ResponseTodoListDto> 모든유저투두카드조회() {
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

                boardDtoList.add(boardDto);
            }

            userResponseDto.setBoardDtoList(boardDtoList);
            userDtoList.add(userResponseDto);
        }
        return userDtoList;
    }
    @Transactional
    public Board 카드추가(Board board, UserDetailsImpl userDetails){
        board.setUser(userDetails.getUser());
        return boardRepository.save(board);
    }
    @Transactional
    public Board 게시글조회(long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public Board 게시글수정(long id, Board user){

        Board userEntity = boardRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        userEntity.setTitle(user.getTitle());
        userEntity.setContents(user.getContents());
        //user.setCreatedDate(user.getCreatedDate());
        boardRepository.save(userEntity);
        return userEntity;
    }
    @Transactional
    public void 게시글삭제(long boardId) {
        boardRepository.deleteById(boardId);
    }
}

