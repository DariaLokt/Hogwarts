package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FacultyController facultyController;

    String host = "http://localhost:";
    String st = "/student";

    @Test
    @DisplayName("Показывает всех студентов")
    void getAllStudentsTest() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject(host + port + st + "/getAll", String.class))
                .isNotNull();
    }

    @Test
    @DisplayName("Добавляет студента")
    void addStudentTest() throws Exception {
//        given
        Student testStudent = new Student();
        testStudent.setId(0L);
        testStudent.setName("Test");
        testStudent.setAge(110);

//        when&then
        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                .isNotNull();

//        delete test
        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
    }

    @Test
    @DisplayName("Показывает студента")
    void getStudentTest() throws Exception {
//        given
            Student testStudent = new Student();
            testStudent.setId(0L);
            testStudent.setName("Test");
            testStudent.setAge(110);

//        when
            Assertions
                    .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                    .isNotNull();

        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

//        then
            ResponseEntity<Student> result = restTemplate.exchange(
                    host + port + st + "/get?id=" + testedStudent.getId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Student>() {
                    }
            );
            assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
            assertEquals(testedStudent,result.getBody());

//        delete test
            ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                    host + port + st + "/delete?id=" + testedStudent.getId(),
                    HttpMethod.DELETE,
                    null,
                    new ParameterizedTypeReference<Faculty>() {
                    }
            );
    }

    @Test
    @DisplayName("Редактирует информацию студента")
    void editStudentInfoTest() throws Exception {
        //given
        Student testStudent = new Student();
        testStudent.setId(0L);
        testStudent.setName("Test");
        testStudent.setAge(110);

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                .isNotNull();

        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

        Student newTestedStudent = testedStudent;
        newTestedStudent.setAge(60);

//        when
        ResponseEntity<Student> result = restTemplate.exchange(
                host + port + st + "/edit",
                HttpMethod.PUT,
                new HttpEntity<>(newTestedStudent),
                new ParameterizedTypeReference<Student>() {
                }
        );

        ResponseEntity<Student> result2 = restTemplate.exchange(
                host + port + st + "/get?id=" + newTestedStudent.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertEquals(new ResponseEntity<>(newTestedStudent,HttpStatus.OK), new ResponseEntity<>(result2.getBody(),result2.getStatusCode()));

//        delete test
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                host + port + st + "/delete?id=" + newTestedStudent.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
    }

    @Test
    @DisplayName("Удаляет студента")
    void deleteStudentTest() throws Exception {
        //given
        Student testStudent = new Student();
        testStudent.setId(0L);
        testStudent.setName("Test");
        testStudent.setAge(110);

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                .isNotNull();

        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

//        when
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
        ResponseEntity<Student> result = restTemplate.exchange(
                host + port + st + "/get?id=" + testedStudent.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );

        //then
        assertEquals(HttpStatus.valueOf(200), studentResponseEntity.getStatusCode());
        assertEquals(HttpStatus.valueOf(404), result.getStatusCode());
    }

    @Test
    @DisplayName("Ищет всех студентов определённого возраста")
    void getStudentsByAgeTest() throws Exception {
        //given
        Student testStudent = new Student();
        testStudent.setId(0L);
        testStudent.setName("Test");
        testStudent.setAge(110);

        Student testStudent2 = new Student();
        testStudent2.setId(0L);
        testStudent2.setName("Test2");
        testStudent2.setAge(110);

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                .isNotNull();

        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent2,String.class))
                .isNotNull();

        Student testedStudent2 = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test2"))
                .findFirst().orElseThrow();

        Collection<Student> students = new HashSet<>();
        students.add(testedStudent);
        students.add(testedStudent2);
        int age = testedStudent.getAge();

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                host + port + st + "/getByAge?age=" + age,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertArrayEquals(students.toArray(),result.getBody().toArray());

//        delete test
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
        ResponseEntity<Student> studentResponseEntity2 = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent2.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
    }

    @Test
    @DisplayName("Ищет студентов в возрасте от и до")
    void findStudentsByAgeGapTest() throws Exception {
        //given
        Student testStudent = new Student();
        testStudent.setId(0L);
        testStudent.setName("Test");
        testStudent.setAge(100);

        Student testStudent2 = new Student();
        testStudent2.setId(0L);
        testStudent2.setName("Test2");
        testStudent2.setAge(110);

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
                .isNotNull();

        Student testedStudent = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test"))
                .findFirst().orElseThrow();

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent2,String.class))
                .isNotNull();

        Student testedStudent2 = studentController.getAllStudents().stream()
                .filter(c -> c.getName().equals("Test2"))
                .findFirst().orElseThrow();

        Collection<Student> students = new HashSet<>();
        students.add(testedStudent);
        students.add(testedStudent2);

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                host + port + st + "/findByAgeBetween?min=99&max=111",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertArrayEquals(students.toArray(),result.getBody().toArray());

//        delete test
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
        ResponseEntity<Student> studentResponseEntity2 = restTemplate.exchange(
                host + port + st + "/delete?id=" + testedStudent2.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );
    }

//    @Test
//    @DisplayName("Даёт факультет студента")
//    void getFacultyTest() throws Exception {
////        given
//        Student testStudent = new Student();
//        testStudent.setId(0L);
//        testStudent.setName("Test");
//        testStudent.setAge(11);
//        Faculty testFaculty = new Faculty();
//        testFaculty.setId(0L);
//        testFaculty.setName("TestFac");
//        testFaculty.setColor("TestBlue");
//        testStudent.setFaculty(testFaculty);
//
//        Assertions
//                .assertThat(this.restTemplate.postForObject(host + port + st + "/add",testStudent,String.class))
//                .isNotNull();
//
//        Student testedStudent = studentController.getAllStudents().stream()
//                .filter(c -> c.getName().equals("Test"))
//                .findFirst().orElseThrow();
//
//        Assertions
//                .assertThat(this.restTemplate.postForObject(host + port + "/faculty" + "/add",testFaculty,String.class))
//                .isNotNull();
//
//        Faculty testedFaculty = facultyController.getAllFaculties().stream()
//                .filter(c -> c.getName().equals("TestFac"))
//                .findFirst().orElseThrow();
//
//        Collection<Student> students = new ArrayList<>();
//        students.add(testedStudent);
//        facultyService.setFacultyStudents(students, testedFaculty.getId());
//
////        when
//        ResponseEntity<Faculty> result = restTemplate.exchange(
//                host + port + st + "/getFaculty",
//                HttpMethod.GET,
//                new HttpEntity<>(testedStudent.getId()),
//                new ParameterizedTypeReference<Faculty>() {
//                }
//        );
//
////        then
//        assertEquals(new ResponseEntity<>(testFaculty,HttpStatus.OK), result);
//        Assertions
//                .assertThat(this.restTemplate.getForObject(host + port + st + "/getFaculty", String.class))
//                .isNotNull();
//
////        delete test
//        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
//                host + port + st + "/delete?id=" + testedStudent.getId(),
//                HttpMethod.DELETE,
//                null,
//                new ParameterizedTypeReference<Student>() {
//                }
//        );
//    }
}