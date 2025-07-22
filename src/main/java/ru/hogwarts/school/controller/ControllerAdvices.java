package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.exceptions.InvalidIDException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<Error> handleInvalidIDException
            (InvalidIDException e) {
        Error error = new Error("InvalidIDCode", "Неправильный ID");
        return new ResponseEntity<Error>(error, HttpStatusCode.valueOf(404));
    }
}
