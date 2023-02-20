package com.example.ftp.service;

import com.example.ftp.dto.UserDto;

public interface FtpService {
    void sendFileToFtp(UserDto userDto) throws Exception;
}
