package com.pjh.todoapp.Entity.Comments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pjh.todoapp.Entity.TimeStamped;
import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Comments extends TimeStamped {
    // 중간 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 없으면 안돌아감

    @JsonIgnoreProperties({"board"})
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;
    // 댓글 아이디는 알아야될듯?

    @JoinColumn(name = "boardId")
    @ManyToOne
    private Board board;
    // 해당 board의 정보를 알아야 저장할듯

    @Column(length = 100, nullable= false)
    private String content;
    // 댓글 내용
}
