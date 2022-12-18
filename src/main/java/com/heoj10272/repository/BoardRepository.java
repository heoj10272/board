package com.heoj10272.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.heoj10272.entity.Board;
import com.heoj10272.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b" +
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

    //--------------------------------------------------------------------------

    @Query("select b, bf from Board b " + "left outer join BoardFile bf on bf.board = b")
    Page<Object[]> getListPage(Pageable pageable);

    @Query(value ="SELECT b, w, count(r), count(bf) " +
            "FROM Board b " +
            "lEFT JOIN b.writer w " +
            "LEFT JOIN Reply r ON r.board = b " +
            "LEFT JOIN BoardFile bf on bf.board = b " +
            "group by b ")
    Page<Object[]> getListPage1(Pageable pageable);

    @Query("SELECT b, w, bf, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b" +
            " LEFT OUTER JOIN BoardFile bf ON bf.board = b" +
            " WHERE b.bno = :bno" +
            " GROUP BY bf")
    List<Object[]> getBoardWithAll(Long bno);
}