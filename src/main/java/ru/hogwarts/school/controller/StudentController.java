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

    @GetMapping("/getAll")
    public Set<Student> getAllStudents() {
        return studentService.getAll();
    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/get")
    public Student getStudent(@RequestParam("id") Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/edit")
    public Student editStudentInfo(@RequestBody Student student) {
        return studentService.editStudentInformation(student);
    }

    @DeleteMapping("/delete")
    public Student deleteStudent(@RequestParam("id") Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/getByAge")
    public Set<Student> getStudentsByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }
}
