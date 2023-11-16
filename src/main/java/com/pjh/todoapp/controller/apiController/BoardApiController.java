package com.pjh.todoapp.controller.apiController;

import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.controller.dto.BoardPostDto;
import com.pjh.todoapp.controller.dto.BoardUpdateDto;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.util.ApiRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.pjh.todoapp.service.BoardService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/user")
    public String addCard(@RequestBody BoardPostDto userPostDto,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("게시글 작성 컨트롤러");
        boardService.카드추가(userPostDto.toEntity(), userDetails);
        return "redirect:/";
    }

    @GetMapping("/user/{id}")
    //@ResponseBody
    public Board showDetails(@PathVariable int id){
        System.out.println("showDetails 확인");
        return boardService.게시글조회(id);
    }

    @PutMapping("/user/{boardId}")
    //@ResponseBody
    public ResponseEntity<?> updatePost(
            @PathVariable int boardId,
            @RequestBody BoardUpdateDto boardUpdateDto){
        try {
            Board updatedUser = boardService.게시글수정(boardId, boardUpdateDto.toEntity());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ApiRequestException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
        }
    }

    @DeleteMapping("/user/{boardId}")
    public ResponseEntity<?> deletePost(
            @PathVariable int boardId){
        System.out.println("여긴 문제가 아닌거 같은데?");
        try {
            boardService.게시글삭제(boardId);
            return new ResponseEntity<>("게시물이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (ApiRequestException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
        }
    }
}
