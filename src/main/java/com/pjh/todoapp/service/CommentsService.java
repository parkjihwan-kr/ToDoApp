package com.pjh.todoapp.service;

import com.pjh.todoapp.Entity.Comments.Comments;
import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Handler.ex.CustomValidationApiException;
import com.pjh.todoapp.Repository.CommentsRepository;
import com.pjh.todoapp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    @Transactional
    public Comments 댓글남기기(String contents, long boardId, long userId){
        Board board = new Board();
        board.setId(boardId);

        User userEntity = userRepository.findById(userId).orElseThrow(()-> {
            throw new CustomValidationApiException("해당 유저를 찾을 수 없습니다.");
        });

        Comments comments = new Comments();
        comments.setBoard(board);
        comments.setUser(userEntity);
        comments.setContent(contents);

        System.out.println(comments.getBoard().getId());
        System.out.println(comments.getUser().getId());
        System.out.println(comments.getContent());

        return commentsRepository.save(comments);
    }

    @Transactional
    public void 댓글삭제(long boardId){
        commentsRepository.deleteById(boardId);
    }
}
