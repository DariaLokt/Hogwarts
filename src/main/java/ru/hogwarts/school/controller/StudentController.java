package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
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
    public void deleteStudent(@RequestParam("id") Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/getByAge")
    public Collection<Student> getStudentsByAge(@RequestParam("age") int age) {
        return studentService.getByAge(age);
    }

    @GetMapping("/findByAgeBetween")
    public Collection<Student> findStudentsByAgeGap(@RequestParam("min") int min, @RequestParam("max") int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/getFaculty")
    public Faculty getFaculty(@RequestParam("id") Long id) {
        return studentService.getFaculty(id);
    }

}
