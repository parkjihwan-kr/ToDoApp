package com.pjh.todoapp.controller.dto;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDeleteDto {
    public Board toEntity(){
        return Board.builder()
                .build();
    }
}
