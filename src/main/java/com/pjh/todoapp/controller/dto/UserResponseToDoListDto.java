package com.pjh.todoapp.controller.dto;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseToDoListDto {
    private String username;
    private long userId;
    private List<BoardResponseDto> boardDtoList;

    public UserResponseToDoListDto(){

    }
    public UserResponseToDoListDto(List<BoardResponseDto> boardDtoList){
        this.boardDtoList = boardDtoList;
    }
}
