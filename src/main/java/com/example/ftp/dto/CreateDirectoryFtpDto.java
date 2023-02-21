package com.example.ftp.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateDirectoryFtpDto {

    @NotBlank
    private String path;

}
