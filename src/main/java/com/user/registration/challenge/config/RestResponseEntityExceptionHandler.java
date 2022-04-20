package com.user.registration.challenge.config;

import com.user.registration.challenge.error.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).collect(Collectors.toList());

        return handleExceptionInternal(ex, BAD_REQUEST, request, validationList.toString());
    }

    @ExceptionHandler({
            InvalidRequestException.class,
            InvalidPasswordException.class
    })
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, BAD_REQUEST, request);
    }


    @ExceptionHandler({
            IpAPIException.class
    })
    protected ResponseEntity<Object> handleInternalServerErrorException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({
            InvalidIPAddressException.class
    })
    protected ResponseEntity<Object> handleForbiddenException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, FORBIDDEN, request);
    }


    private ResponseEntity<Object> handleExceptionInternal(Exception exception, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(exception, new ApiErrorDTO(status, exception.getMessage()), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception exception, HttpStatus status, WebRequest request, String validationMessage) {
        return handleExceptionInternal(exception, new ApiErrorDTO(status, validationMessage), new HttpHeaders(), status, request);
    }

}
