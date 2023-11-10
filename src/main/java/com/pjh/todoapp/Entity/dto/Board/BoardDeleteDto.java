package com.pjh.todoapp.Entity.dto.Board;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDeleteDto {
    String deletePassword;

    public Board toEntity(){
        return Board.builder()
                .password(deletePassword)
                .build();
    }
}
