package com.pjh.todoapp.controller.dto;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseUserToDoListDto {
    private String username;
    private List<Board> boardList;

    public ResponseUserToDoListDto(String username, List<Board> boardList){
        this.username = username;
        this.boardList = boardList;
    }
}
