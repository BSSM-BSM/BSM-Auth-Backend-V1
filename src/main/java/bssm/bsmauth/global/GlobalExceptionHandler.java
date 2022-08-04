package bssm.bsmauth.global;

import bssm.bsmauth.global.exceptions.HttpError;
import bssm.bsmauth.global.exceptions.HttpErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpError.class)
    public ResponseEntity<HttpErrorResponse> handleException(HttpError exception) {
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(exception);
        return new ResponseEntity<>(httpErrorResponse, HttpStatus.valueOf(exception.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(new HttpError());
        return new ResponseEntity<>(httpErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}