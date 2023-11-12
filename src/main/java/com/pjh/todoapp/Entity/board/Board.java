package com.pjh.todoapp.Entity.board;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    // board가 password를 가지고 있는게..? 추후 put, delete작업 할때 삭제 예정

    private String contents;

    private LocalDateTime createdDate;


    @PrePersist            // db에 INSERT되기 직전에 실행
    public void createDate() {
        this.createdDate = LocalDateTime.now();
    }
}
