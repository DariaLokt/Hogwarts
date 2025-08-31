package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for adding faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for getting faculty by id");
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new InvalidIDException(id);
        });
    }

    public Faculty editFacultyInformation(Faculty faculty) {
        logger.info("Was invoked method for editing faculty info");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for deleting faculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getByColor(String color) {
        logger.info("Was invoked method for getting all faculties with specific color");
        return facultyRepository.findAll().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toSet());
    }

    public Set<Faculty> getAll() {
        logger.info("Was invoked method for getting all faculties");
        return new HashSet<>(facultyRepository.findAll());
    }

    public Faculty findByName(String name) {
        logger.info("Was invoked method for getting faculty with specific name ignoring case");
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Faculty findByColor(String color) {
        logger.info("Was invoked method for getting faculty with specific color ignoring case");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Student> getFacultyStudents(Long id) {
        logger.info("Was invoked method for getting faculty students");
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new InvalidIDException(id);
        }).getStudentsOfFaculty();
    }
}
