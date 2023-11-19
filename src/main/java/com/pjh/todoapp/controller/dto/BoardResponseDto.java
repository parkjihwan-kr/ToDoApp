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
    private boolean checkState;
    // 완료여부를 위해 get방식으로 controller에서 뿌려줄때 받아와야함
    private LocalDateTime createdAt;
}
