package com.example.ftp.controller;

import com.example.ftp.dto.UserDto;
import com.example.ftp.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ftp")
public class FtpController {

    final FtpService ftpService;
    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping
    public ResponseEntity sendUserFtp (@RequestBody UserDto userDto) throws Exception {
        var fileToFtp = ftpService.sendFileToFtp(userDto);
        return ResponseEntity.ok().body(fileToFtp);
    }

}
