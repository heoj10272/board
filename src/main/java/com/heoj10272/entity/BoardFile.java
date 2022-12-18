package com.heoj10272.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "board")
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    private String uuid;

    private String fileName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
