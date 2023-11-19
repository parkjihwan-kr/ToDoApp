package com.pjh.todoapp.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestCommentDto {

    private String content;
}
