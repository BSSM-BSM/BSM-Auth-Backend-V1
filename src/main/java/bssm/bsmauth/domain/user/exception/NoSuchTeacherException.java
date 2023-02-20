package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchTeacherException extends NotFoundException {
    public NoSuchTeacherException() {
        super("선생님을 찾을 수 없습니다.");
    }
}