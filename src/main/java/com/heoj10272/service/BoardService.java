package com.heoj10272.service;

import com.heoj10272.dto.BoardDTO;
import com.heoj10272.dto.BoardFileDTO;
import com.heoj10272.dto.PageRequestDTO;
import com.heoj10272.dto.PageResultDTO;
import com.heoj10272.entity.BoardFile;
import com.heoj10272.entity.Board;
import com.heoj10272.entity.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    BoardDTO getBoard(Long bno);

    void removeWithReplies(Long bno);

    void modify(BoardDTO boardDTO);

//    default Board dtoToEntity(BoardDTO dto){
//
//        Member member = Member.builder().email(dto.getWriterEmail()).build();
//
//        Board board = Board.builder()
//                .bno(dto.getBno())
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .writer(member)
//                .build();
//        return board;
//    }

    default Map<String, Object> dtoToEntity(BoardDTO boardDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder().email(boardDTO.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(member)
                .build();

        entityMap.put("board", board);

        List<BoardFileDTO> fileDTOList = boardDTO.getFileDTOList();

        if(fileDTOList != null && fileDTOList.size() > 0 ) { //MovieImageDTO 처리

            List<BoardFile> boardFileList = fileDTOList.stream().map(boardFileDTO ->{

                //bno 빠짐
                BoardFile boardFile = BoardFile.builder()
                        .path(boardFileDTO.getPath())
                        .fileName(boardFileDTO.getFileName())
                        .uuid(boardFileDTO.getUuid())
                        .board(board)
                        .build();
                return boardFile;
            }).collect(Collectors.toList());

            entityMap.put("fileList", boardFileList);
        }

        return entityMap;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        return boardDTO;

    }

    default BoardDTO entitiesToDTO(Board board, Member member, List<BoardFile> boardFiles, Long replyCount, Long boardFileCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        List<BoardFileDTO> boardFileDTOList = boardFiles.stream().map(boardFile -> {
            return BoardFileDTO.builder().fileName(boardFile.getFileName())
                    .path(boardFile.getPath())
                    .uuid(boardFile.getUuid())
                    .build();
        }).collect(Collectors.toList());

        boardDTO.setFileDTOList(boardFileDTOList);

        return boardDTO;
    }

    default BoardDTO entitiesToDTO1(Board board, Member member, Long replyCount, Long boardFileCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .boardFileCount(boardFileCount.intValue())
                .build();

        return boardDTO;
    }

    default BoardDTO entitiesToDTO2(Board board, Member member, List<BoardFile> boardFiles, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        System.out.println("Checking .............,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        System.out.println(boardFiles.toString().contains("BoardFile"));

        if (boardFiles.toString().contains("BoardFile") != true){
            return  boardDTO;
        }

        System.out.println("Entering .............,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        List<BoardFileDTO> boardFileDTOList = boardFiles.stream().map(boardFile -> {
            return BoardFileDTO.builder()
                    .fileName(boardFile.getFileName())
                    .path(boardFile.getPath())
                    .uuid(boardFile.getUuid())
                    .build();
        }).collect(Collectors.toList());

        System.out.println("Complete .............,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");

        boardDTO.setFileDTOList(boardFileDTOList);

        return boardDTO;
    }

}
