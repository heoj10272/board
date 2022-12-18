package com.heoj10272.repository;

import com.heoj10272.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    @Modifying
    @Query("delete from BoardFile bf where bf.board.bno =:bno ")
    void deleteByBno(Long bno);

}
