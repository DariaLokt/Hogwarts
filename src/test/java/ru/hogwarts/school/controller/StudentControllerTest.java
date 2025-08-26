package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomUtils.insecure;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    void beforeEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    String getURL(String end) {
        return "http://localhost:" + port + "/student" + end;
    }

    List<Student> fillRepositoryUp() {
        return Stream.generate(() -> studentRepository.save(new Student()))
                .limit(nextInt(10,15))
                .toList();
    }

    Student getOneOfStudents(List<Student> repository) {
        return repository.get(insecure().randomInt(0,repository.size()));
    }

    Student getTestStudent(String name, int age, Faculty faculty) {
        Student test = new Student();
        test.setId(0);
        test.setName(name);
        test.setAge(age);
        test.setFaculty(faculty);
        return test;
    }

    Student getTestStudent(String name, int age) {
        Student test = new Student();
        test.setId(0);
        test.setName(name);
        test.setAge(age);
        return test;
    }

    @Test
    @DisplayName("Показывает всех студентов")
    void getAllStudentsTest() throws Exception {
//        given
        List<Student> expected = fillRepositoryUp();

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                getURL("/getAll"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody())
                .isNotNull()
                .isNotEmpty()
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Добавляет студента")
    void addStudentTest() throws Exception {
//        given
        List<Student> repository = fillRepositoryUp();
        Student expected = getTestStudent("Test",10);

//        when
        ResponseEntity<Student> result = restTemplate.exchange(
                getURL("/add"),
                HttpMethod.POST,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<Student>() {
                }
        );

        expected.setId(result.getBody().getId());

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Показывает студента")
    void getStudentTest() throws Exception {
//        given
        List<Student> repository = fillRepositoryUp();
        Student expected = getOneOfStudents(repository);

//        when
        ResponseEntity<Student> result = restTemplate.exchange(
                getURL("/get?id=")+expected.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Редактирует информацию студента")
    void editStudentInfoTest() throws Exception {
//        given
        List<Student> repository = fillRepositoryUp();
        Student initial = getOneOfStudents(repository);
        Student expected = new Student();
        expected.setId(initial.getId());
        expected.setName("Test");
        expected.setAge(1000);

//        when
        ResponseEntity<Student> result = restTemplate.exchange(
                getURL("/edit"),
                HttpMethod.PUT,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<Student>() {
                }
        );

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
        assertTrue(studentRepository.findAll().contains(expected));
        assertFalse(studentRepository.findAll().contains(initial));
    }

    @Test
    @DisplayName("Удаляет студента")
    void deleteStudentTest() throws Exception {
        //given
        List<Student> repository = fillRepositoryUp();
        Student expected = getOneOfStudents(repository);

//        when
        ResponseEntity<Student> result = restTemplate.exchange(
                getURL("/delete?id=") + expected.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );

        //then
        assertFalse(studentRepository.findAll().contains(expected));
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
    }

    @Test
    @DisplayName("Ищет всех студентов определённого возраста")
    void getStudentsByAgeTest() throws Exception {
//        given
        List<Student> repository = fillRepositoryUp();
        int age = insecure().randomInt();
        Student test = getTestStudent("Test", age);
        Student test2 = getTestStudent("Test2", age);
        studentRepository.save(test);
        studentRepository.save(test2);
        List<Student> expected = new ArrayList<>();
        expected.add(test);
        expected.add(test2);

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                getURL("/getByAge?age=" + age),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertThat(result).isNotNull();
        assertThat(result.getBody())
                .isNotNull()
                .isNotEmpty()
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Ищет студентов в возрасте от и до")
    void findStudentsByAgeGapTest() throws Exception {
        //given
        List<Student> repository = fillRepositoryUp();
        int age = insecure().randomInt();
        Student test = getTestStudent("Test", insecure().randomInt(age-2,age+2));
        Student test2 = getTestStudent("Test2", insecure().randomInt(age-2,age+2));
        studentRepository.save(test);
        studentRepository.save(test2);
        List<Student> expected = new ArrayList<>();
        expected.add(test);
        expected.add(test2);

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                getURL("/findByAgeBetween?min=" + (age-2) + "&max=" + (age+2)),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertThat(result).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Даёт факультет студента")
    void getFacultyTest() throws Exception {
//        given
        List<Student> repository = fillRepositoryUp();
        Faculty testFac = new Faculty();
        facultyRepository.save(testFac);
        Student test = getTestStudent("Test", 10, testFac);
        Student test2 = getTestStudent("Test2", 11, testFac);
        studentRepository.save(test);
        studentRepository.save(test2);
        List<Student> expected = new ArrayList<>();
        expected.add(test);
        expected.add(test2);
        testFac.setStudentsOfFaculty(expected);
        facultyRepository.save(testFac);
//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/getFaculty?id="+test.getId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertThat(result).isNotNull();
        assertEquals(testFac,result.getBody());
    }
}