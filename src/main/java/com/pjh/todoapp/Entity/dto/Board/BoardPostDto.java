package com.pjh.todoapp.Entity.dto.Board;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostDto {
    private String title;
    //private String password;
    //private String username;
    private String contents;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                //.username(username)
                .contents(contents)
                //.password(password)
                .build();
    }
}
