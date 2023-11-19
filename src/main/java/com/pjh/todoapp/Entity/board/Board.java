package com.pjh.todoapp.Entity.board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pjh.todoapp.Entity.TimeStamped;
import com.pjh.todoapp.Entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"user"})
public class Board extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String contents;

    private boolean checkState;
    // 해당 todo의 완료 상태

    public Board(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
}
