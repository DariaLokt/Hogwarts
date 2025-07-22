package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students;
    private int counter;

    public StudentService() {
        this.students = new HashMap<>();
        this.counter = 0;
    }

    public Student addStudent(Student student) {
        students.put(student.getId(), student);
        counter++;
        return student;
    }

    public Student getStudent(Long id) {
        return students.get(id);
    }

    public Student editStudentInformation(Student student) {
        students.get(student.getId())
                .setName(student.getName());
        students.get(student.getId())
                .setAge(student.getAge());
        return student;
    }

    public String deleteStudent(Long id) {
        students.remove(id);
        counter--;
        return "Студент с id " + id + " удалён";
    }

    public Set<Student> getByAge(int age) {
        return students.values().stream()
                .filter(st -> st.getAge() == age)
                .collect(Collectors.toSet());
    }

    public int getCounter() {
        return counter;
    }
}
