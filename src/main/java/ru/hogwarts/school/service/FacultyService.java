package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
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
        faculties.put(faculty.getId(), faculty);
        counter++;
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        return faculties.get(id);
    }

    public Faculty editFacultyInformation(Faculty faculty) {
        faculties.get(faculty.getId())
                .setName(faculty.getName());
        faculties.get(faculty.getId())
                .setColor(faculty.getColor());
        return faculty;
    }

    public String deleteFaculty(Long id) {
        faculties.remove(id);
        counter--;
        return "Факультет с id " + id + " удалён";
    }

    public Set<Faculty> getByColor(String color) {
        return faculties.values().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toSet());
    }

    public int getCounter() {
        return counter;
    }
}
