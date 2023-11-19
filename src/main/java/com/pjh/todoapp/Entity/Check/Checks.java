package com.pjh.todoapp.Entity.Check;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pjh.todoapp.Entity.TimeStamped;
import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Checks extends TimeStamped {
    // 중간 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnoreProperties({"board"})
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JoinColumn(name = "boardId")
    @ManyToOne
    private Board board;

    @NotNull
    private boolean completedToDo;
    // 해당 게시글에 대하여 완료했다는 체크 여부
}
