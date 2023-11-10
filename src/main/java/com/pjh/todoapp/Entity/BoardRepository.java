package com.pjh.todoapp.Board;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAll(Sort sort);
    List<Board> findAllById(int id);
}
