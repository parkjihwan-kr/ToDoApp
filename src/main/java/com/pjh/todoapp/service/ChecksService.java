package com.pjh.todoapp.service;

import com.pjh.todoapp.Entity.Check.Checks;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.ChecksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChecksService {

    private final ChecksRepository checksRepository;

    @Transactional
    public void 투두완료(int boardId, long loginId){
        checksRepository.complete(boardId, loginId);
        // repository custom function -> complete
        // hibernate : insert into checks(column1, .....) values (value1, .....);
    }

    @Transactional
    public void 투두미완료(int boardId, long loginId){
        checksRepository.unComplete(boardId, loginId);
    }
}
