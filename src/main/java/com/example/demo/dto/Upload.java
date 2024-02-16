package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class Upload {
    private String nmae;
    private MultipartFile file;
}