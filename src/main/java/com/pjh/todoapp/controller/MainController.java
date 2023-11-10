package controller;

import com.pjh.board.Entity.Board.Board;
import com.pjh.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardService userService;
    //private final BoardService boardService;

    @GetMapping({"/","/board/index"})
    public String showMainPage(Model model){
        List<Board> boardList = userService.게시판모든리스트조회();
        model.addAttribute("boardList", boardList);
        return "board/index";
    }
}
/*
- [ ]  게시글 작성 기능
    - `제목`, `작성자명`, `비밀번호`, `작성 내용`, `작성일`을 저장할 수 있습니다.
    - 저장된 게시글의 정보를 반환 받아 확인할 수 있습니다.
        - 반환 받은 게시글의 정보에 `비밀번호`는 제외 되어있습니다.
- [ ]  선택한 게시글 조회 기능                                  o
    - 선택한 게시글의 정보를 조회할 수 있습니다.                   o
        - 반환 받은 게시글의 정보에 `비밀번호`는 제외 되어있습니다.  o
- [ ]  게시글 목록 조회 기능
    - 등록된 게시글 전체를 조회할 수 있습니다.                    o
        - 반환 받은 게시글의 정보에 `비밀번호`는 제외 되어있습니다.  o
    - 조회된 게시글 목록은 작성일 기준 내림차순으로 정렬 되어있습니다. o
- [ ]  선택한 게시글 수정 기능
    - 선택한 게시글의 `제목`, `작성자명`, `작성 내용`을 수정할 수 있습니다.            o
        - 서버에 게시글 수정을 요청할 때 `비밀번호`를 함께 전달합니다.                 o
        - 선택한 게시글의 `비밀번호`와 요청할 때 함께 보낸 `비밀번호`가 일치할 경우에만 수정이 가능합니다.
    - 수정된 게시글의 정보를 반환 받아 확인할 수 있습니다.
        - 반환 받은 게시글의 정보에 `비밀번호`는 제외 되어있습니다.
- [ ]  선택한 게시글 삭제 기능
    - 선택한 게시글을 삭제할 수 있습니다.
        - 서버에 게시글 삭제를 요청할 때 `비밀번호`를 함께 전달합니다.
        - 선택한 게시글의 `비밀번호`와 요청할 때 함께 보낸 `비밀번호`가 일치할 경우에만 삭제가 가능합니다.*/
