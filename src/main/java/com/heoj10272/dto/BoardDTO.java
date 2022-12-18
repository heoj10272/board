package com.heoj10272.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    private String writerEmail; //작성자의 이메일(id)

    private String writerName; //작성자의 이름

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCount; //해당 게시글의 댓글 수

    private int boardFileCount;

    @Builder.Default
    private List<BoardFileDTO> fileDTOList = new ArrayList<>();
}
