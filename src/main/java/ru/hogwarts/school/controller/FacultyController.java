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

    @GetMapping("/read")
    public Faculty getFaculty(@RequestParam("id") Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping("/edit")
    public Faculty editFacultyInfo(@RequestBody Faculty faculty) {
        return facultyService.editFacultyInformation(faculty);
    }

    @PostMapping("/add")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @DeleteMapping("/delete")
    public String deleteFaculty(@RequestParam("id") Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/getbycolor")
    public Set<Faculty> getFacultiesByColor(@RequestParam("color") String color) {
        return facultyService.getByColor(color);
    }
}
