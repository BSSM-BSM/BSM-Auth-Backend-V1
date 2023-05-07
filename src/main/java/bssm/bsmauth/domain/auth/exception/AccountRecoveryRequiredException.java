package bssm.bsmauth.domain.auth.exception;

import bssm.bsmauth.global.error.exceptions.ForbiddenException;

public class AccountRecoveryRequiredException extends ForbiddenException {
    public AccountRecoveryRequiredException() {
        super("로그인 접근이 제한되었습니다. 계정 비밀번호를 복구해주세요.");
    }
}