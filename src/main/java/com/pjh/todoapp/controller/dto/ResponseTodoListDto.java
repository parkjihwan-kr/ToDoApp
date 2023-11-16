package com.pjh.todoapp.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseTodoListDto {
    private String username;
    private long userId;
    private List<BoardResponseDto> boardDtoList;

    public ResponseTodoListDto(){

    }
    public ResponseTodoListDto(List<BoardResponseDto> boardDtoList){
        this.boardDtoList = boardDtoList;
    }
}
