package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchUserException extends NotFoundException {
    public NoSuchUserException() {
        super("유저를 찾을 수 없습니다.");
    }
}