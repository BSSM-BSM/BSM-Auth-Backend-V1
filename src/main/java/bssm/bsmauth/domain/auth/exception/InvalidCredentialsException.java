package bssm.bsmauth.domain.auth.exception;

import bssm.bsmauth.global.error.exceptions.UnAuthorizedException;

public class InvalidCredentialsException extends UnAuthorizedException {
    public InvalidCredentialsException() {
        super("ID 또는 비밀번호가 맞지 않습니다");
    }
}