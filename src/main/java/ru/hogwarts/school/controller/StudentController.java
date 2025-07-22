package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/read")
    public Student getStudent(@RequestParam("id") Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/edit")
    public Student editStudentInfo(@RequestBody Student student) {
        return studentService.editStudentInformation(student);
    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @DeleteMapping("/delete")
    public String deleteStudent(@RequestParam("id") Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/getbyage")
    public Set<Student> getStudentsByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }
}
