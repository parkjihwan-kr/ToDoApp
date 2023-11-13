package com.pjh.todoapp.controller;

import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final BoardService boardService;

    @GetMapping({"/","/board/index"})
    public String showMainPage(Model model,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("유저 투두 리스트 조회 컨트롤러");
        Map<String, List<String>> todoUserList = boardService.유저투두카드조회();
        System.out.println("===============유저 투두 리스트 조회==============");
        for (Map.Entry<String, List<String>> entry : todoUserList.entrySet()) {
            String username = entry.getKey();
            List<String> titles = entry.getValue();

            System.out.println("Username: " + username);
            System.out.println("Titles: " + titles);
        }
        model.addAttribute("todoUserList", todoUserList);
        model.addAttribute("username", userDetails.getUser().getUsername());
        // hello-card에 username 추가
        return "board/board";
    }
}
