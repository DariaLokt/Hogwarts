package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.InvalidIDException;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.HashSet;
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
        counter++;
        return students.put(student.getId(), student);
    }

    public Student getStudent(Long id) {
        if (students.containsKey(id)) {
            return students.get(id);
        } else {
            throw new InvalidIDException();
        }
    }

    public Student editStudentInformation(Student student) {
        return students.put(student.getId(), student);
    }

    public Student deleteStudent(Long id) {
        if (students.containsKey(id)) {
            counter--;
            return students.remove(id);
        } else {
            throw new InvalidIDException();
        }
    }

    public Set<Student> getAll() {
        return new HashSet<>(students.values());
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
