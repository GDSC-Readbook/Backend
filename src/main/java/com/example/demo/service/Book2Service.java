package com.example.demo.service;

import com.example.demo.entity.Book2;
import com.example.demo.repository.Book2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class Book2Service {

    private final Book2Repository bookRepository;

    public List<Book2> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book2> findById(Long id) throws Exception {
        try {
            return bookRepository.findById(id);
        } catch (Exception e) {
            throw new Exception("책을 찾을 수 없습니다.");
        }
    }

    public Boolean save(Book2 book) {
        try {
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}