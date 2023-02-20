package com.example.ftp.controller;

import com.example.ftp.dto.UserDto;
import com.example.ftp.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("ftp")
public class FtpController {

    final FtpService ftpService;
    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping
    public ResponseEntity sendUserFtp (@RequestBody UserDto userDto) throws Exception {
//        createArray(userDto);
        ftpService.sendFileToFtp(userDto);
        return ResponseEntity.ok().body("Success");
    }

//    private static void createArray(UserDto userDto) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(userDto);
//    }
}