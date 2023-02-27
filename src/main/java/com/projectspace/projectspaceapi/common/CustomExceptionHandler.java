package com.projectspace.projectspaceapi.common;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.response.Error;
import com.projectspace.projectspaceapi.common.response.ErrorBody;
import com.projectspace.projectspaceapi.common.response.ForbiddenError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Error> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((error) -> new Error(error.getField(), error.getDefaultMessage()))
                .toList();

        ErrorBody error = new ErrorBody(errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyTakenException.class)
    public final ResponseEntity<Object> handleAlreadyTaken(
            AlreadyTakenException exception, WebRequest request) {

        ErrorBody error = new ErrorBody(List.of(new Error("entity", exception.getMessage())));


        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFound(
            NotFoundException exception, WebRequest request) {

        ErrorBody error = new ErrorBody(List.of(new Error("entity", exception.getMessage())));


        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<Object> handleForbidden(
            ForbiddenException exception, WebRequest request) {

        ErrorBody error = new ErrorBody(List.of(new ForbiddenError()));


        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

