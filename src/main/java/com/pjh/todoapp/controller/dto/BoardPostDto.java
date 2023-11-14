package com.pjh.todoapp.controller.dto;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostDto {
    private String title;
    private String contents;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .contents(contents)
                .build();
    }
}
