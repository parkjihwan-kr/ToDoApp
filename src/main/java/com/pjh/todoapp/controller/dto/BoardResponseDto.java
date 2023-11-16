package com.pjh.todoapp.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponseDto{
    // dto는 timeStramped 상속받을 필요 없음.
    private long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
}
