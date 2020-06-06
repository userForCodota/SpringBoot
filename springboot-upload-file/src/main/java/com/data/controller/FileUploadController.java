package com.data.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileUploadController {
    @PostMapping("/uploadFileController")
    public String fileUpload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//获取文件名称
        file.transferTo(new File("E:/桌面/" + originalFilename));//转移到其他地方，需要处理I/O异常
        return "OK";
    }
}
