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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController {
    @Value("${local.file.path}")
    String uploadFolder;

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

    @Operation(operationId = "/book/save", summary = "책 저장", description = "책을 저장합니다.", tags = "BookController")
    @PostMapping("/book/save")
    public ResponseEntity<Boolean> save(@RequestPart("book") Book book, @RequestPart("file") MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //파일명을 얻어낼 수 있는 메서드!
        long size = file.getSize(); //파일 사이즈

        System.out.println("파일명 : "  + fileRealName);
        System.out.println("용량크기(byte) : " + size);

        String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
        String[] uuids = uuid.toString().split("-");

        String uniqueName = uuids[0];
        System.out.println("생성된 고유문자열" + uniqueName);
        System.out.println("확장자명" + fileExtension);

        File saveFile = new File(uploadFolder + uniqueName + fileExtension);

        file.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
        book.setBookImage(uniqueName + fileExtension);
        return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
    }


        @GetMapping(value="/book/image/{name}", produces= MediaType.IMAGE_PNG_VALUE)
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
