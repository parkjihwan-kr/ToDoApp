package com.pjh.todoapp.controller.apiController;

import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.dto.Board.BoardDeleteDto;
import com.pjh.todoapp.Entity.dto.Board.BoardPostDto;
import com.pjh.todoapp.Entity.dto.Board.BoardUpdateDto;
import com.pjh.todoapp.util.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pjh.todoapp.service.BoardService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/user")
    public String writePost(@RequestBody BoardPostDto userPostDto) {
        Board board = boardService.게시글작성(userPostDto.toEntity());
        return "redirect:/";
    }

    @GetMapping("/user/{id}")
    //@ResponseBody
    public Board showDetails(@PathVariable int id){
        return boardService.게시글조회(id);
    }

    @PutMapping("/user/{id}")
    //@ResponseBody
    public ResponseEntity<?> updatePost(
            @PathVariable int id,
            @RequestBody BoardUpdateDto boardUpdateDto){
        try {
            Board updatedUser = boardService.게시글수정(id, boardUpdateDto.toEntity());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ApiRequestException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable int id,
            @RequestBody BoardDeleteDto boardDeleteDto){
        try {
            boardService.게시글삭제(boardDeleteDto.toEntity(), id);
            return new ResponseEntity<>("게시물이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (ApiRequestException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
        }
    }
}
