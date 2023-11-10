package com.pjh.todoapp.Board.dto;

import com.pjh.board.Entity.Board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostDto {
    private String title;
    private String password;
    private String username;
    private String contents;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .contents(contents)
                .username(username)
                .password(password)
                .build();
    }
}
