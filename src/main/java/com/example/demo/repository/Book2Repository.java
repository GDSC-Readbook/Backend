package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface Book2Repository extends JpaRepository<Book, Long> {
    Optional<Book> findById(Long id);
    Optional<Book> findByBookName(String bookName);
    Optional<Book> findByBookTitle(String bookTitle);
    Optional<Book> findByBookAuthor(String bookAuthor);
    Optional<Book> findByBookContent(String bookContent);

}