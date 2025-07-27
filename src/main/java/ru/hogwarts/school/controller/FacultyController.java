package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Set;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/getAll")
    public Set<Faculty> getAllFaculties() {
        return facultyService.getAll();
    }

    @PostMapping("/add")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/get")
    public Faculty getFaculty(@RequestParam("id") Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping("/edit")
    public Faculty editFacultyInfo(@RequestBody Faculty faculty) {
        return facultyService.editFacultyInformation(faculty);
    }

    @DeleteMapping("/delete")
    public void deleteFaculty(@RequestParam("id") Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/getByColor")
    public Set<Faculty> getFacultiesByColor(@RequestParam("color") String color) {
        return facultyService.getByColor(color);
    }
}
