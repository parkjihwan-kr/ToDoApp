package com.pjh.todoapp.Repository;

import com.pjh.todoapp.Entity.Check.Checks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChecksRepository extends JpaRepository<Checks, Long> {

    @Modifying
    @Query(value = "INSERT INTO checks(boardId, userId, createdAt, completedToDo) values(:boardId, :loginId, now(), true)", nativeQuery = true)
    int complete(int boardId, long loginId);

    @Modifying
    @Query(value="DELETE FROM checks WHERE boardId = :boardId AND userId = :loginId", nativeQuery=true)
    int unComplete(int boardId, long loginId);
}
