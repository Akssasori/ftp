package com.example.ftp.controller;

import com.example.ftp.dto.CreateDirectoryFtpDto;
import com.example.ftp.dto.UserDto;
import com.example.ftp.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("directory")
    public ResponseEntity createDirectory(@RequestBody CreateDirectoryFtpDto directoryFtpDto) throws Exception {
        ftpService.createDirectory(directoryFtpDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("list-files")
    public void listFiles() throws Exception {
        ftpService.listFiles();
    }

//    @GetMapping("download")
//    public void download() throws Exception {
//        ftpService.download();
//    }

}
