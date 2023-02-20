package bssm.bsmauth.domain.auth.exception;

import bssm.bsmauth.global.error.exceptions.NotFoundException;

public class NoSuchTokenException extends NotFoundException {
    public NoSuchTokenException() {
        super("토큰을 찾을 수 없습니다.");
    }
}