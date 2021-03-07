package com.github.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HAN
 * @version 1.0
 * @create 03-08-3:22
 */
@Controller
@Slf4j
public class FileUploadController {

    @GetMapping("/form_layouts")
    public String form_layouts() {
        return "form/form_layouts";
    }

    /**
     *  获取上传的文件用 @RequestPart + MultiPartFile || MultiPartFIle[]
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos) {
        log.info("username={}, email={}, headerImg={}, photos={}", username,
                email, headerImg.getSize(), photos.length);
        return "main";
    }

}
