package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Book2Controller {
    @Value("${local.file.path}")
    String uploadFolder;

    private final BookService bookService;

    @Operation(operationId = "/book2/findAll", summary = "모든 책 조회", description = "모든 책을 조회합니다.", tags = "BookController")
    @GetMapping("/book2/findAll")
    public ResponseEntity<List<Book>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @Operation(operationId = "/book2/findById", summary = "책 조회", description = "책을 조회합니다.", tags = "BookController")
    @GetMapping("/book2/findById")
    public ResponseEntity<Book> findById(@RequestParam Long id) throws Exception {
        return new ResponseEntity<>(bookService.findById(id).orElseThrow(() -> new Exception("책을 찾을 수 없습니다.")), HttpStatus.OK);
    }

    @Operation(operationId = "/book2/save", summary = "책 저장", description = "책을 저장합니다.", tags = "BookController")
    @PostMapping("/book2/save")
    public ResponseEntity<Boolean> save(@RequestBody Book book) throws Exception {
        book.setBookImage("example.png");
        return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
    }


        @GetMapping(value="/book2/image/{name}", produces= MediaType.IMAGE_PNG_VALUE)
        public @ResponseBody byte[] getImage(@PathVariable String name)
                throws IOException{
            FileInputStream fis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String fileDir = uploadFolder+name; // 파일경로

            try{
                fis = new FileInputStream(fileDir);
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }

            int readCount = 0;
            byte[] buffer = new byte[1024];
            byte[] fileArray = null;

            try{
                while((readCount = fis.read(buffer)) != -1){
                    baos.write(buffer, 0, readCount);
                }
                fileArray = baos.toByteArray();
                fis.close();
                baos.close();
            } catch(IOException e){
                throw new RuntimeException("File Error");
            }
            return fileArray;
        }

}
