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
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        logger.info("Was invoked method for getting students' average age");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for getting last five students");
        return studentRepository.getLastFiveStudents();
    }

    public Collection<String> getByFirstLetter(String letter) {
        logger.info("Was invoked method for getting students by first letter of name");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(st -> st.startsWith(letter.toUpperCase()))
                .sorted()
                .toList();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for getting students' average age through stream");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average().orElseThrow();
    }

    public Long sum() {
        logger.info("Sum1");
        long startTime1 = System.nanoTime();
        long sum1 = LongStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Long::sum);
        long endTime1 = System.nanoTime();

        logger.info("Sum2");
        long startTime2 = System.nanoTime();
        long sum2 = LongStream.rangeClosed(1,1_000_000)
                .parallel()
                .reduce(0, Long::sum);
        long endTime2 = System.nanoTime();

        logger.info("Sum1 time: " + (endTime1 - startTime1));
        logger.info("Sum2 time: " + (endTime2 - startTime2));
        logger.info("sum1 = " + sum1);
        logger.info("sum2 = " + sum2);

        return sum2;
    }
}
