package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String bookName;
    private String bookTitle;
    private String bookAuthor;
    private String bookImage;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String bookContent;

}