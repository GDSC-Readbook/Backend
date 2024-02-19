package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class Book2Service {

    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) throws Exception {
        try {
            return bookRepository.findById(id);
        } catch (Exception e) {
            throw new Exception("책을 찾을 수 없습니다.");
        }
    }

    public Boolean save(Book book) {
        try {
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}