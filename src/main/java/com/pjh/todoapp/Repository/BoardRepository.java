package com.pjh.todoapp.Repository;

import com.pjh.todoapp.Entity.board.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAll(Sort sort);
    List<Board> findAllById(int id);
}
