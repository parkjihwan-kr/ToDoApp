package com.pjh.todoapp.Repository;

import com.pjh.todoapp.Entity.board.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAll(Sort sort);
    // Username을 String 리스트로 가져오는 메서드
}
