package bssm.bsmauth.domain.auth.exception;

import bssm.bsmauth.global.error.exceptions.BadRequestException;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AlreadyUsedAuthCodeException extends BadRequestException {

    private static Map<String, String> createMsg() {
        return ImmutableMap.<String, String>builder().
                put("authCode", "이미 사용된 인증코드입니다").
                build();
    }

    public AlreadyUsedAuthCodeException() {
        super(createMsg());
    }
}