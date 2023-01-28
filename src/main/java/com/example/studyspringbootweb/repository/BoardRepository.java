package com.example.studyspringbootweb.repository;

import com.example.studyspringbootweb.domain.Board;
import com.example.studyspringbootweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
