package com.pjh.todoapp.controller;

import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardService boardService;
    //private final BoardService boardService;

    @GetMapping({"/","/board/index"})
    public String showMainPage(Model model){
        List<Board> boardList = boardService.게시판모든리스트조회();
        model.addAttribute("boardList", boardList);
        return "board/index";
    }
}
