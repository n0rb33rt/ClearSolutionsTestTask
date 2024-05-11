package com.norbert.clearsolutions.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(value = {BadRequestException.class,UserNotFoundException.class })
    public ResponseEntity<ApiException> handleBadRequestException(RuntimeException exception){
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiException> handleSqlException(SQLException ex) {
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .message(ex.getLocalizedMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiException> handleMissingParameter(MissingServletRequestParameterException exception) {
        String parameterName = exception.getParameterName();
        String message = "Required parameter is missing: " + parameterName;
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {MethodArgumentNotValidException.class })
    public ResponseEntity<ApiException> handleBadRequestException(MethodArgumentNotValidException exception){
        ApiException apiException = ApiException.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .message(exception.getBody().getDetail())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
}