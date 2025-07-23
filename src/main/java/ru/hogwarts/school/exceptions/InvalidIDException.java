package ru.hogwarts.school.exceptions;

public class InvalidIDException extends RuntimeException {
    public InvalidIDException() {
        super("Нет такого id");
    }
}
