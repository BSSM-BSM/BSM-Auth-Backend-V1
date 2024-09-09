package bssm.bsmauth.domain.auth.service;

import bssm.bsmauth.global.mail.MailService;
import bssm.bsmauth.global.mail.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMailProvider {

    private final MailService mailService;

    public void sendAuthCodeMail(String email, String authCode) {
        String escapedAuthCode = StringEscapeUtils.escapeHtml4(authCode);
        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.app/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM 회원가입 인증 코드입니다.</h1>\n" +
                "                <h2 style=\"display:inline-block;font-size:20px;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">"+ escapedAuthCode +"</h2>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                    <p style=\"margin:0;\">- 인증 코드는 한 사람당 한 개의 계정에만 쓸 수 있습니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2023. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                .to(email)
                .subject("BSM 회원가입 인증 코드입니다")
                .content(content)
                .build();
        mailService.sendMail(mailDto);
    }

    public void sendFindIdMail(String email, String id) {
        String escapedId = StringEscapeUtils.escapeHtml4(id);
        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.app/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM ID 복구 메일입니다</h1>\n" +
                "                <h2 style=\"display:inline-block;font-size:20px;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">"+ escapedId +"</h2>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2023. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                .to(email)
                .subject("BSM ID 복구 메일입니다")
                .content(content)
                .build();
        mailService.sendMail(mailDto);
    }

    public void sendResetPwMail(String email, String token) {
        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.app/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM 비밀번호 재설정 링크입니다</h1>\n" +
                "                <a href=\"https://auth.bssm.app/resetPw?token=" + token + "\" style=\"display:inline-block;font-size:20px;text-decoration:none;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">비밀번호 재설정</a>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                    <p style=\"margin:0;\">- 해당 링크는 발송시점으로 부터 5분동안 유효합니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2023. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                .to(email)
                .subject("BSM 비밀번호 재설정 링크입니다")
                .content(content)
                .build();
        mailService.sendMail(mailDto);
    }
}
