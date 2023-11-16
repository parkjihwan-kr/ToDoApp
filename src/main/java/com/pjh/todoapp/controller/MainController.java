package com.pjh.todoapp.controller;

import com.pjh.todoapp.controller.dto.ResponseTodoListDto;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final BoardService boardService;

    @GetMapping({"/","/board/index"})
    public String showMainPage(Model model,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ResponseTodoListDto> todoUserList = boardService.모든유저투두카드조회();

        model.addAttribute("todoUserList", todoUserList);
        model.addAttribute("loginUsername", userDetails.getUser().getUsername());
        // hello-card에 username 추가
        return "board/board";
    }
}
