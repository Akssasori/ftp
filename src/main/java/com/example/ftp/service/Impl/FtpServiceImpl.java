package com.example.ftp.service.Impl;

import com.example.ftp.config.FTPConfiguration;
import com.example.ftp.dto.CreateDirectoryFtpDto;
import com.example.ftp.dto.SendFileFtpDto;
import com.example.ftp.dto.UserDto;
import com.example.ftp.service.FtpService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Service
@Log4j2
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
    public boolean sendFileToFtp(UserDto userDto) throws Exception {

        SendFileFtpDto build = SendFileFtpDto.builder()
                .fileName(buildNameFile(userDto))
                .bytes(createFile(userDto)).build();

        FTPConfiguration config = getFtpConfiguration();
        FTPClient ftpClient = loginFtp(config);
        InputStream inputStream = new ByteArrayInputStream(build.getBytes());
        boolean uploadFile = uploadFile(inputStream, config, ftpClient, build);
        logoutFtp(ftpClient);
        return uploadFile;

    }

    @Override
    public void createDirectory(CreateDirectoryFtpDto directoryFtpDto) throws Exception {
        FTPConfiguration config = getFtpConfiguration();
        FTPClient ftpClient = loginFtp(config);
        log.info("[createDirectory]{} Is success to create directory : {} -> {}",
                System.currentTimeMillis(), path, ftpClient.makeDirectory(directoryFtpDto.getPath()));
        logoutFtp(ftpClient);


    }

    @Override
    public void listFiles() throws Exception {

        FTPConfiguration config = getFtpConfiguration();
        FTPClient ftpClient = loginFtp(config);
        ftpClient.enterLocalPassiveMode();

        FTPFile[] list = ftpClient.listFiles(config.getPath());
        for (FTPFile f : list) {
            System.out.println(f.getName());
        }
        logoutFtp(ftpClient);

    }

//    @Override
//    public byte[] download() throws Exception {
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        FTPConfiguration config = getFtpConfiguration();
//        FTPClient ftpClient = loginFtp(config);
//        ftpClient.enterLocalPassiveMode();
//
//        boolean b = ftpClient.retrieveFile(config.getPath(), byteArrayOutputStream);
//        logoutFtp(ftpClient);
//        return byteArrayOutputStream.toByteArray();
//    }

    public FTPClient loginFtp(FTPConfiguration config) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
            @Override
            public void protocolCommandSent(ProtocolCommandEvent protocolCommandEvent) {
                log.info("{}{} Command sent : {}-{}", Thread.currentThread().getName(), System.currentTimeMillis(), protocolCommandEvent.getCommand(), protocolCommandEvent.getMessage());
            }

            @Override
            public void protocolReplyReceived(ProtocolCommandEvent protocolCommandEvent) {
                log.info("{}{} Reply received : {}", Thread.currentThread().getName(), System.currentTimeMillis(), protocolCommandEvent.getMessage());
            }
        });

        if (Boolean.FALSE.equals(ftpClient.isConnected())) {

            ftpClient.connect(config.getHost(), Integer.parseInt(config.getPort()));
            ftpClient.login(config.getUsername(), config.getPassword());
            log.info(ftpClient.getReplyString());
        }

        return ftpClient;
    }

    private void logoutFtp(FTPClient ftpClient) throws IOException {
        log.info("disconnecting from ftp server...");
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();

        }
    }

    public boolean uploadFile(InputStream inputStream, FTPConfiguration config, FTPClient ftpClient, SendFileFtpDto build) throws Exception {

        boolean storeFile = false;
        if (ftpClient.isConnected()) {
            ftpClient.enterLocalPassiveMode();
            storeFile = ftpClient.storeFile(config.getPath() + build.getFileName(), inputStream);
            log.info("uploadFile {} Is success to upload file : {} -> {}", System.currentTimeMillis(),
                    build.getFileName(), storeFile);

        }

        return storeFile;
    }

    private String buildNameFile(UserDto userDto) {

        StringJoiner joiner = new StringJoiner("");
        joiner.add(userDto.getName().toLowerCase());
        joiner.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        joiner.add(".dat");
        return joiner.toString();
    }

    private byte[] createFile(UserDto userDto) {

        StringJoiner stringJoiner = new StringJoiner("");
        stringJoiner.add(userDto.getEmail());
        stringJoiner.add(userDto.getName());
        stringJoiner.add(userDto.getPassword());
        return stringJoiner.toString().getBytes();
    }

    private FTPConfiguration getFtpConfiguration() {
        return FTPConfiguration.builder()
                .host(host)
                .password(password)
                .port(port)
                .path(path)
                .username(username)
                .build();
    }

}
