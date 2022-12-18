package com.heoj10272.service;

import com.heoj10272.dto.BoardDTO;
import com.heoj10272.dto.PageRequestDTO;
import com.heoj10272.dto.PageResultDTO;
import com.heoj10272.entity.BoardFile;
import com.heoj10272.repository.BoardFileRepository;
import com.heoj10272.repository.BoardRepository;
import com.heoj10272.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.heoj10272.entity.Board;
import com.heoj10272.entity.Member;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final ReplyRepository replyRepository;
    private final BoardFileRepository fileRepository;


//    @Override
//    public Long register(BoardDTO dto) {
//
//        log.info(dto);
//
//        Board board = dtoToEntity(dto);
//
//        repository.save(board);
//
//        return board.getBno();
//    }

    @Override
    @Transactional
    public Long register(BoardDTO boardDTO) {

        Map<String, Object> entityMap = dtoToEntity(boardDTO);
        Board board = (Board) entityMap.get("board");
        List<BoardFile> boardFileList = (List<BoardFile>) entityMap.get("fileList");

        repository.save(board);

        if(boardFileList != null) {
            boardFileList.forEach(boardFile -> {
                fileRepository.save(boardFile);
            });
        }

        return board.getBno();
    }

//    @Override
//    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
//
//        log.info(pageRequestDTO);
//
//        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));
//
////        Page<Object[]> result = repository.getBoardWithReplyCount(
////                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
//        Page<Object[]> result = repository.searchPage(
//                pageRequestDTO.getType(),
//                pageRequestDTO.getKeyword(),
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
//
//        return new PageResultDTO<>(result, fn);
//    }


    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO requestDTO) {

        log.info(requestDTO);

        Function<Object[], BoardDTO> fn = (arr -> entitiesToDTO1(
                (Board)arr[0] ,
                (Member)arr[1],
                (Long)arr[2],
                (Long)arr[3])
        );

        //Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());

        //Page<Object[]> result = repository.getListPage1(pageable);

        Page<Object[]> result = repository.searchPage(
                requestDTO.getType(),
                requestDTO.getKeyword(),
                requestDTO.getPageable(Sort.by("bno").descending())  );

        log.info("==============================================");
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });


        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);
        fileRepository.deleteByBno(bno);

        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getOne(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
        }
    }

    @Override
    public BoardDTO getBoard(Long mno) {

        List<Object[]> result = repository.getBoardWithAll(mno);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }

        Board board = (Board) result.get(0)[0];
        Member member = (Member) result.get(0)[1];

        List<BoardFile> boardFileList = new ArrayList<>();

        result.forEach(arr -> {
            BoardFile  boardFile = (BoardFile)arr[2];
            boardFileList.add(boardFile);
        });

        Long replyCount = (Long) result.get(0)[3];

        log.info("replyCount : "+replyCount);

        return entitiesToDTO2(board, member, boardFileList, replyCount);
    }

}
