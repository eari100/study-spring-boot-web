package com.example.studyspringbootweb.service;

import com.example.studyspringbootweb.domain.Board;
import com.example.studyspringbootweb.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> findBoardList(Pageable pageable) {
        int page = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(page, pageable.getPageSize());

        return boardRepository.findAll(pageable);
    }

    public Board findBoardIdx(Long id) {
        return boardRepository.findById(id).orElse(new Board());
    }
}
