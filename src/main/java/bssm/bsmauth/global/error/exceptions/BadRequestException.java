package bssm.bsmauth.global.error.exceptions;

import bssm.bsmauth.global.error.HttpError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BadRequestException extends HttpError {

    private final int statusCode = 400;
    private final Map<String, String> fields;
}