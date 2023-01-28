package com.example.studyspringbootweb;

import com.example.studyspringbootweb.domain.Board;
import com.example.studyspringbootweb.domain.Users;
import com.example.studyspringbootweb.domain.enums.BoardType;
import com.example.studyspringbootweb.repository.BoardRepository;
import com.example.studyspringbootweb.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void init() {
        Users users = usersRepository.save(
                Users.builder()
                        .name("havi")
                        .password("test")
                        .email(email)
                        .createdDate(LocalDateTime.now())
                        .build());

        boardRepository.save(
                Board.builder()
                        .title(boardTestTitle)
                        .subTitle("서브 타이틀")
                        .content("콘텐츠")
                        .boardType(BoardType.free)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .users(users)
                        .build());
    }

    @Test
    public void 제대로_생성됐는지_테스트() {
        Users users = usersRepository.findByEmail(email);
        assertThat(users.getName(), is("havi"));
        assertThat(users.getPassword(), is("test"));
        assertThat(users.getEmail(), is(email));

        Board board = boardRepository.findByUsers(users);
        assertThat(board.getTitle(), is(boardTestTitle));
        assertThat(board.getSubTitle(), is("서브 타이틀"));
        assertThat(board.getContent(), is("콘텐츠"));
        assertThat(board.getBoardType(), is(BoardType.free));
    }
}
