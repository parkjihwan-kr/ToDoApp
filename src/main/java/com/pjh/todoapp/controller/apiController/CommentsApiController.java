package com.pjh.todoapp.controller.apiController;

import com.pjh.todoapp.Entity.CMRespDto;
import com.pjh.todoapp.Entity.Comments.Comments;
import com.pjh.todoapp.Handler.ex.CustomValidationApiException;
import com.pjh.todoapp.controller.dto.RequestCommentDto;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.service.CommentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentsApiController {

    private final CommentsService commentsService;

    @PostMapping("/user/comments/{boardId}")
    public ResponseEntity<?> leaveComment(@Valid @RequestBody RequestCommentDto requestCommentDto,
                                          BindingResult bindingResult,
                                          @PathVariable long boardId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("===댓글남기기 메서드 시작===");
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationApiException("유효한 형식이 아닙니다.", errorMap);
        }
        System.out.println("댓글 남기기 controller");
        Comments comments = commentsService.saveComments(requestCommentDto.getContent(), boardId, userDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(200,"suceess",comments), HttpStatus.OK);
    }

    @DeleteMapping("/user/comments/{boardId}")
    public ResponseEntity<?> commentDelete(@PathVariable long boardId){
        commentsService.deleteComment(boardId);
        return new ResponseEntity<>(new CMRespDto<>(1,"댓글 삭제 성공",null), HttpStatus.OK);
    }
}
