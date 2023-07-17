package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDTO> validation(MethodArgumentNotValidException exception) {
        return exception.getFieldErrors().stream().map(fe -> new ErrorDTO(fe.getDefaultMessage(), fe.getField())).toList();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<List<ErrorDTO>> dataIntegrityViolationException(DataIntegrityViolationException exception) {
        if (exception.getCause() instanceof ConstraintViolationException e && e.getConstraintName().toLowerCase().contains("word_relation_unique")) {
            return ResponseEntity.status(409).body(Collections.singletonList(new ErrorDTO("Duplicate relation")));
        }
        return exception(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<List<ErrorDTO>> exception(Exception exception) {
        log.error("Unexpected exception occured", exception);
        return ResponseEntity.internalServerError().body(singletonList(new ErrorDTO("Unexpected error occured. Please contact customer support.")));
    }
}
