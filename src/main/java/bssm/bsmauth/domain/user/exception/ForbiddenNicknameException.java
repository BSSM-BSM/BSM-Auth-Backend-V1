package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.ForbiddenException;

public class ForbiddenNicknameException extends ForbiddenException {
    public ForbiddenNicknameException() {
        super("사용할 수 없는 닉네임입니다.");
    }
}