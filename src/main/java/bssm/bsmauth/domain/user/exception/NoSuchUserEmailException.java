package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchUserEmailException extends NotFoundException {
    public NoSuchUserEmailException() {
        super("유저의 이메일을 찾을 수 없습니다.");
    }
}