package bssm.bsmauth.global.auth.exception;

import bssm.bsmauth.global.error.exceptions.ForbiddenException;

public class InvalidApiTokenException extends ForbiddenException {
    public InvalidApiTokenException() {
        super("API 요청이 유효하지 않거나 만료되었습니다");
    }
}