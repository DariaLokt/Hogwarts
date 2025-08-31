package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.InvalidIDException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for adding student");
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for getting student by id");
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new InvalidIDException(id);
        });
    }

    public Student editStudentInformation(Student student) {
        logger.info("Was invoked method for editing student's information");
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for deleting student");
        studentRepository.deleteById(id);
    }

    public Set<Student> getAll() {
        logger.info("Was invoked method for getting all the students");
        return new HashSet<>(studentRepository.findAll());
    }

    public Collection<Student> getByAge(int age) {
        logger.info("Was invoked method for getting students of specific age");
        return studentRepository.findAll().stream()
                .filter(st -> st.getAge() == age)
                .collect(Collectors.toSet());
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for finding students falling into age gap");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for getting student's faculty");
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new InvalidIDException(id);
        }).getFaculty();
    }

    public Long getAmountOfStudents() {
        logger.info("Was invoked method for getting amount of students");
        return studentRepository.getAmountOfStudents();
    }

    public Integer getAverageAgeOfStudents() {
        logger.info("Was invoked method for getting student's average age");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for getting last five students");
        return studentRepository.getLastFiveStudents();
    }
}
