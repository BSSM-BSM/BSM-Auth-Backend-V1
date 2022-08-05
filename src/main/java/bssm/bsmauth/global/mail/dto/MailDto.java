package bssm.bsmauth.global.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailDto {

    private String to;
    private String subject;
    private String content;
}
