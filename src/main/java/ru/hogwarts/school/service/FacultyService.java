package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.InvalidIDException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(InvalidIDException::new);
    }

    public Faculty editFacultyInformation(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Set<Faculty> getByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toSet());
    }

    public Set<Faculty> getAll() {
        return new HashSet<>(facultyRepository.findAll());
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Faculty findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Student> getFacultyStudents(Long id) {
        return facultyRepository.findById(id).orElseThrow(InvalidIDException::new).getStudentsOfFaculty();
    }
}
