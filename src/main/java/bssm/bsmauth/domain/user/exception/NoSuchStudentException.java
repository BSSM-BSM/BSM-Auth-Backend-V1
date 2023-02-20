package bssm.bsmauth.domain.user.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchStudentException extends NotFoundException {
    public NoSuchStudentException() {
        super("학생을 찾을 수 없습니다.");
    }
}