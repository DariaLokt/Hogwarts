package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("analysis")
public class AnalysisController {
    private final StudentService studentService;
    private final FacultyService facultyService;
    private final AvatarService avatarService;

    public AnalysisController(StudentService studentService, FacultyService facultyService, AvatarService avatarService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
        this.avatarService = avatarService;
    }

    @GetMapping("/getAmountOfStudents")
    public Long getAmountOfStudents() {
        return studentService.getAmountOfStudents();
    }

    @GetMapping("/getAverageAgeOfStudents")
    public Integer getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/getLastFiveStudents")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}
