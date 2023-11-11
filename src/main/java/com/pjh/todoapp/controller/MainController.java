package com.pjh.todoapp.controller;

import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.dto.web.SignupRequestDto;
import com.pjh.todoapp.Handler.ex.CustomValidationApiException;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.service.BoardService;
import com.pjh.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardService boardService;

    @GetMapping({"/","/board/index"})
    public String showMainPage(Model model,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Board> boardList = boardService.게시판모든리스트조회();
        model.addAttribute("boardList", boardList);
        return "board/board";
    }
}
