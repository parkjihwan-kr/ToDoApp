package com.pjh.todoapp.controller.dto;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateDto {
    private String updateTitle;
    private String updateContents;

    public Board toEntity() {
        return Board.builder()
                .title(updateTitle)
                .contents(updateContents)
                .build();
    }

    // Getter 및 Setter 메소드
    // dto getter,setter필요함.
}
