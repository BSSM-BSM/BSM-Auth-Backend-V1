package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.global.error.exceptions.BadRequestException;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.regex.Pattern;

@Component
public class OauthFacade {

    public String getRandomStr(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }

    public void uriCheck(String domain, String uri) {
        if (!Pattern.matches("(https?://)("+domain+")(:(6[0-5]{2}[0-3][0-5]|[1-5][0-9]{4}|[1-9][0-9]{0,3}))?/.*", uri)) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("redirectURI", "리다이렉트 주소가 올바르지 않습니다").
                    build()
            );
        }
    }

}
