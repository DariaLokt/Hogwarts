package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hogwarts.school.exceptions.InvalidIDException;

@RestControllerAdvice
public class ControllerAdvise {
    @ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<Error> handleInvalidIDException
            (InvalidIDException e) {
        Error error = new Error("InvalidIDCode", "Неправильный id");
        return new ResponseEntity<Error>(error, HttpStatusCode.valueOf(404));
    }
}
