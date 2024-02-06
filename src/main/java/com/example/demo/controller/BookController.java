package com.example.demo.controller;

import com.example.demo.dto.SignRequest;
import com.example.demo.dto.SignResponse;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.EmailService;
import com.example.demo.service.SignService;
import com.example.demo.unit.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(operationId = "/book/findAll", summary = "모든 책 조회", description = "모든 책을 조회합니다.", tags = "BookController")
    @GetMapping("/book/findAll")
    public ResponseEntity<List<Book>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @Operation(operationId = "/book/findById", summary = "책 조회", description = "책을 조회합니다.", tags = "BookController")
    @GetMapping("/book/findById")
    public ResponseEntity<Book> findById(@RequestParam Long id) throws Exception {
        return new ResponseEntity<>(bookService.findById(id).orElseThrow(() -> new Exception("책을 찾을 수 없습니다.")), HttpStatus.OK);
    }

}
