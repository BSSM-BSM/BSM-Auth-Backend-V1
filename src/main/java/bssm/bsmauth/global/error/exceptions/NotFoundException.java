package bssm.bsmauth.global.error.exceptions;

import bssm.bsmauth.global.error.HttpError;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotFoundException extends HttpError {

    private final int statusCode = 404;
    private String message = "Not Found";

    public NotFoundException(String message) {
        this.message = message;
    }
}