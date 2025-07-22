package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.InvalidIDException;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties;
    private int counter;

    public FacultyService() {
        this.faculties = new HashMap<>();
        this.counter = 0;
    }

    public Faculty addFaculty(Faculty faculty) {
        counter++;
        return faculties.put(faculty.getId(), faculty);
    }

    public Faculty getFaculty(Long id) {
        if (faculties.containsKey(id)) {
            return faculties.get(id);
        } else {
            throw new InvalidIDException();
        }
    }

    public Faculty editFacultyInformation(Faculty faculty) {
        return faculties.put(faculty.getId(), faculty);
    }

    public Faculty deleteFaculty(Long id) {
        counter--;
        return faculties.remove(id);
    }

    public Set<Faculty> getByColor(String color) {
        return faculties.values().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toSet());
    }

    public Set<Faculty> getAll() {
        return new HashSet<>(faculties.values());
    }

    public int getCounter() {
        return counter;
    }
}
