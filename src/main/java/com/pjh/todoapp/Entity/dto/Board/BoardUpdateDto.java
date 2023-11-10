package com.pjh.todoapp.Entity.dto.Board;

import com.pjh.todoapp.Entity.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateDto {
    private String updateTitle;
    private String updatePassword;
    private String updateUsername;
    private String updateContents;

    public Board toEntity() {
        return Board.builder()
                .username(updateUsername)
                .password(updatePassword)
                .title(updateTitle)
                .contents(updateContents)
                .build();
    }

    // Getter 및 Setter 메소드
    // dto getter,setter필요함.
}
