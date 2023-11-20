package com.pjh.todoapp.service;

import com.pjh.todoapp.Entity.Check.Checks;
import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.ChecksRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChecksService {

    private final ChecksRepository checksRepository;
    private final BoardRepository boardRepository;
    @Transactional
    public void 투두완료(long boardId, User user){
        boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + boardId));
        List<Board> boardList= boardRepository.findByUser(user);
        for (Board board : boardList){
            System.out.println("board.isCheckState() : "+ board.isCheckState());
        }

        checksRepository.complete(boardId, user.getId());
        // repository custom function -> complete
        // hibernate : insert into checks(column1, .....) values (value1, .....);
    }

    @Transactional
    public void 투두미완료(long boardId, long loginId){
        System.out.println("삭제");
        checksRepository.unComplete(boardId, loginId);
    }
}
