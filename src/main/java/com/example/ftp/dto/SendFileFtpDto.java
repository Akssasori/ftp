package com.example.ftp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendFileFtpDto {
    private byte[] bytes;
    private String fileName;

}
