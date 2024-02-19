package com.example.demo.repository;

import com.example.demo.entity.Book;
import com.example.demo.entity.Book2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface Book2Repository extends JpaRepository<Book2, Long> {
    Optional<Book2> findById(Long id);
    Optional<Book2> findByBookName(String bookName);
    Optional<Book2> findByBookTitle(String bookTitle);
    Optional<Book2> findByBookAuthor(String bookAuthor);
    Optional<Book2> findByBookContent(String bookContent);

}