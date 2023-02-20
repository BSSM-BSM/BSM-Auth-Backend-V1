package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchUserNameException extends NotFoundException {
    public NoSuchUserNameException() {
        super("유저의 이름을 찾을 수 없습니다.");
    }
}