package com.heoj10272.repository;

import com.heoj10272.entity.Board;
import com.heoj10272.entity.BoardFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardFileRepositoryTests {

    @Autowired
    private BoardFileRepository boardFileRepository;

    @Test
    public void insertBoardFile() {

        IntStream.rangeClosed(2,100).forEach(i -> {

            long bno  = i;

            Board board = Board.builder().bno(bno).build();


            System.out.println(UUID.randomUUID().toString());
            System.out.println(bno);
            BoardFile boardFile = BoardFile.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName("test" + i + ".jpg")
                    .path("/test/")
                    .board(board)
                    .build();

            boardFileRepository.save(boardFile);
        });

    }
}
