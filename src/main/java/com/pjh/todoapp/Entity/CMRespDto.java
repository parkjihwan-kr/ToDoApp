package com.pjh.todoapp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CMRespDto<T> {
    private int httpStatusCode; // HttpStatusCode -> int rowStatus
    private String message;     // 해당 처리에 따른 message 출력
    private T data;             // Fail시 -> errorMap저장

}
