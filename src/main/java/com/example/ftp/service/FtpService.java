package com.example.ftp.service;

import com.example.ftp.dto.CreateDirectoryFtpDto;
import com.example.ftp.dto.UserDto;

public interface FtpService {
    boolean sendFileToFtp(UserDto userDto) throws Exception;

    void createDirectory(CreateDirectoryFtpDto directoryFtpDto) throws Exception;

    void listFiles() throws Exception;

//    byte[] download() throws Exception;
}
