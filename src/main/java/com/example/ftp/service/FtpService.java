package com.example.ftp.service;

import com.example.ftp.dto.UserDto;

public interface FtpService {
    boolean sendFileToFtp(UserDto userDto) throws Exception;
}
