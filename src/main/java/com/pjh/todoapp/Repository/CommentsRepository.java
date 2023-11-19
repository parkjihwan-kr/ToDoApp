package com.pjh.todoapp.Repository;

import com.pjh.todoapp.Entity.Comments.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
