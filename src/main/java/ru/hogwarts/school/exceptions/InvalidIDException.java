package ru.hogwarts.school.exceptions;

public class InvalidIDException extends RuntimeException {
    public InvalidIDException(Long id) {
        super("Нет такого id: " + id);
    }
}
