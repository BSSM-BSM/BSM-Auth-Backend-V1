package bssm.bsmauth.global.auth;

import bssm.bsmauth.global.error.HttpError;
import bssm.bsmauth.global.error.HttpErrorResponse;
import bssm.bsmauth.global.error.ValidationErrorResponse;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthFilterExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) {
        try {
            filterChain.doFilter(req, res);
        } catch (BadRequestException e) {
            exceptionHandler(res, e);
        } catch (HttpError e) {
            exceptionHandler(res, e);
        } catch (Exception e) {
            exceptionHandler(res);
        }
    }

    private void exceptionHandler(HttpServletResponse res, HttpError exception) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(exception.getStatusCode());
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(exception)));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exceptionHandler(HttpServletResponse res, BadRequestException exception) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(exception.getStatusCode());
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new ValidationErrorResponse(exception.getFields())));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exceptionHandler(HttpServletResponse res) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(new InternalServerException())));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
