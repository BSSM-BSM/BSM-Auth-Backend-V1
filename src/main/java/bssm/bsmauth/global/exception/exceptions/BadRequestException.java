package bssm.bsmauth.global.exception.exceptions;

import bssm.bsmauth.global.exception.HttpError;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadRequestException extends HttpError {

    private final int statusCode = 400;
    private String message = "Bad Request";

    public BadRequestException(String message) {
        this.message = message;
    }
}