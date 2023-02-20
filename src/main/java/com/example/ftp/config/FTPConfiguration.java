package com.example.ftp.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FTPConfiguration {

    private String host;
    private String port;
    private String username;
    private String password;
    private String path;

}
