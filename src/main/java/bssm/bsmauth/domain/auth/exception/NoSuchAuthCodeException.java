package bssm.bsmauth.domain.auth.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchAuthCodeException extends NotFoundException {
    public NoSuchAuthCodeException() {
        super("인증코드를 찾을 수 없습니다.");
    }
}