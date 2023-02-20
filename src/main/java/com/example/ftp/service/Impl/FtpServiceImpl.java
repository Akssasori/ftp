package com.example.ftp.service.Impl;


import com.example.ftp.config.FTPConfiguration;
import com.example.ftp.dto.SendFileFtpDto;
import com.example.ftp.dto.UserDto;
import com.example.ftp.service.FtpService;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Service
public class FtpServiceImpl implements FtpService {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private String port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.path}")
    private String path;

    @Override
    public void sendFileToFtp(UserDto userDto) throws Exception {

        SendFileFtpDto build = SendFileFtpDto.builder().fileName(buildNameFile(userDto)).bytes(createFile(userDto)).build();
        System.out.println(build);

        FTPConfiguration config = getFtpConfiguration();
        FTPClient ftpClientLogin = loginFtp(config);
        System.out.println(ftpClientLogin.getReplyString());
        InputStream inputStream = new ByteArrayInputStream(build.getBytes());
        var success = uploadFile(inputStream, config, ftpClientLogin, build);

//        deleteIfExists(build.getFileName(), config);


    }

//    private boolean deleteIfExists(String filename, FTPConfiguration config) {
//        return true;
//    }

    private FTPConfiguration getFtpConfiguration() {

        return FTPConfiguration.builder().host(host).password(password).port(port).path(path).username(username).build();
    }

    private byte[] createFile(UserDto userDto) {

        StringJoiner stringJoiner = new StringJoiner("");
        stringJoiner.add(userDto.getEmail());
        stringJoiner.add(userDto.getName());
        stringJoiner.add(userDto.getPassword());
        return stringJoiner.toString().getBytes();
    }

    private String buildNameFile(UserDto userDto) {

        StringJoiner joiner = new StringJoiner("");
        joiner.add(userDto.getName().toLowerCase());
        joiner.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        joiner.add(".dat");
        return joiner.toString();
    }

    public FTPClient loginFtp(FTPConfiguration config) throws Exception {
        FTPClient ftpClient = new FTPClient();
//        ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
//            @Override
//            public void protocolCommandSent(ProtocolCommandEvent protocolCommandEvent) {
//                System.out.printf("[%s][%d] Command sent : [%s]-%s", Thread.currentThread().getName(), System.currentTimeMillis(), protocolCommandEvent.getCommand(), protocolCommandEvent.getMessage());
//            }
//
//            @Override
//            public void protocolReplyReceived(ProtocolCommandEvent protocolCommandEvent) {
//                System.out.printf("[%s][%d] Reply received : %s", Thread.currentThread().getName(), System.currentTimeMillis(), protocolCommandEvent.getMessage());
//            }
//        });
        ftpClient.connect(config.getHost(), Integer.parseInt(config.getPort()));
        ftpClient.login(config.getUsername(), config.getPassword());
//        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }

    //    public void createDirectory(String path, FTPClient ftpClient) throws Exception {
//        System.out.println();
//        System.out.printf("[createDirectory][%d] Is success to create directory : %s -> %b",
//                System.currentTimeMillis(), path, ftpClient.makeDirectory(path));
//        System.out.println();
//        ftpClient.makeDirectory(path);
//    }
    public boolean uploadFile(InputStream inputStream, FTPConfiguration config, FTPClient ftpClientLogin, SendFileFtpDto build) throws Exception {

        boolean storeFile = false;
        if (ftpClientLogin.isConnected()) {
            ftpClientLogin.enterLocalPassiveMode();
            storeFile = ftpClientLogin.storeFile(config.getPath() + build.getFileName(), inputStream);
            System.out.printf("[uploadFile][%d] Is success to upload file : %s -> %b", System.currentTimeMillis(),
                    build.getFileName(), storeFile);

        }

        return storeFile;
    }

}
