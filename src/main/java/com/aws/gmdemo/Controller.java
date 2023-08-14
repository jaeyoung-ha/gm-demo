package com.aws.gmdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping
public class Controller {
    private Environment env;

    AmazonS3Helper amazonS3Helper;

    @Autowired
    public Controller(Environment env, AmazonS3Helper amazonS3Helper) {
        this.env = env;
        this.amazonS3Helper = amazonS3Helper;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("Gentle Monster Demo - Port: %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/upload/file")
    public String uploadFile(@RequestPart("file") MultipartFile multipartFile) {

        // Store Image file to S3
        String fileName = amazonS3Helper.store(multipartFile);

        return "Upload File - Success : fileName" + fileName;
    }

    @GetMapping("/getPhoto/{filename}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("filename") String filename) {
        log.info("/getPhoto - filename : " + filename);

        byte[] photoImg = new byte[0];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        if (TextUtils.isEmpty(filename) || "undefined".equals(filename)) {
            return new ResponseEntity<>(photoImg, headers, HttpStatus.BAD_REQUEST);
        }

        try {
            photoImg = amazonS3Helper.getFile(filename);
        } catch (IOException e) {
            log.error("/getPhoto occurred IOException");
        }

        return new ResponseEntity<>(photoImg, headers, HttpStatus.OK);
    }


}
