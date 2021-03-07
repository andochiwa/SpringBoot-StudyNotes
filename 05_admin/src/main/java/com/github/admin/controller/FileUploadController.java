package com.github.admin.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
     *  @SneakyThrows 处理模板的try-catch, 不用再自己写
     */
    @SneakyThrows
    @PostMapping("/upload")
    public String upload(@RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos) {
        log.info("username={}, email={}, headerImg={}, photos={}", username,
                email, headerImg.getSize(), photos.length);
        if (!headerImg.isEmpty()) {
            // 保存到文件服务器，OSS服务器等
//            headerImg.getInputStream();
            // 获取名字
            String filename = headerImg.getOriginalFilename();
            headerImg.transferTo(new File("E:\\" + filename));
        }

        if (photos.length != 0) {
            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()) {
                    String filename = photo.getOriginalFilename();
                    photo.transferTo(new File("E:\\" + filename));
                }
            }
        }
        return "main";
    }

}
