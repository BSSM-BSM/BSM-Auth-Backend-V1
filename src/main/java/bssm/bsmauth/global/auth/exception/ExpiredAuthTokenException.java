package bssm.bsmauth.global.auth.exception;

import bssm.bsmauth.global.error.exceptions.UnAuthorizedException;

public class ExpiredAuthTokenException extends UnAuthorizedException {
    public ExpiredAuthTokenException() {
        super("로그인 정보가 만료되었습니다");
    }
}