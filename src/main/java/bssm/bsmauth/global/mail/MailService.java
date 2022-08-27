package bssm.bsmauth.global.mail;

import bssm.bsmauth.global.exception.exceptions.InternalServerException;
import bssm.bsmauth.global.mail.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${env.mail.from}")
    private String MAIL_FROM;

    public void sendMail(MailDto mailDto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setSubject(MimeUtility.encodeText(mailDto.getSubject(), "UTF-8", "B"));
            mimeMessageHelper.setText(mailDto.getContent(), true);
            mimeMessageHelper.setFrom(new InternetAddress(MAIL_FROM, MAIL_FROM, "UTF-8"));
            mimeMessageHelper.setTo(new InternetAddress(mailDto.getTo(), mailDto.getTo(), "UTF-8"));

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("메일 전송에 실패하였습니다");
        }
    }
}