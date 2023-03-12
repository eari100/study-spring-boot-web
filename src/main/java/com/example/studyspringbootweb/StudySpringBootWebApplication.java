package com.example.studyspringbootweb;

import com.example.studyspringbootweb.domain.Board;
import com.example.studyspringbootweb.domain.Users;
import com.example.studyspringbootweb.domain.enums.BoardType;
import com.example.studyspringbootweb.repository.BoardRepository;
import com.example.studyspringbootweb.repository.UsersRepository;
import com.example.studyspringbootweb.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class StudySpringBootWebApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBootWebApplication.class, args);
    }

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Bean
    public CommandLineRunner runner(UsersRepository usersRepository, BoardRepository boardRepository) {
        return args -> {
            Users users = usersRepository.save(
                    Users.builder()
                            .name("havi")
                            .password("test")
                            .email("havi@gmail.com")
                            .createdDate(LocalDateTime.now())
                            .build());

                    IntStream.rangeClosed(1, 200).forEach(index ->
                            boardRepository.save(
                                    Board.builder()
                                            .title("게시물"+index)
                                            .subTitle("순서"+index)
                                            .content("콘텐츠")
                                            .boardType(BoardType.free)
                                            .createdDate(LocalDateTime.now())
                                            .updatedDate(LocalDateTime.now())
                                            .users(users)
                                            .build()));
        };
    }
}
