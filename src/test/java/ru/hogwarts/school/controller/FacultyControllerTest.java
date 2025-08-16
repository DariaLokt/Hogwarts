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

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    String host = "http://localhost:";
    String fc = "/faculty";

    @Test
    @DisplayName("Возвращает все факульеты")
    void getAllFacultiesTest() {
        Assertions
                .assertThat(this.restTemplate.getForObject(host + port + fc + "/getAll", String.class))
                .isNotNull();
    }

    @Test
    @DisplayName("Добавляет факультет")
    void addFacultyTest() {
        //        given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

//        when&then
        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

//        delete test
        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Возвращает факультет по id")
    void getFacultyTest() {
        //        given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

//        when
        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();

//        then
        ResponseEntity<Faculty> result = restTemplate.exchange(
                host + port + fc + "/get?id=" + testedFaculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertEquals(testedFaculty,result.getBody());

//        delete test
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Изменяет информацию о факультете")
    void editFacultyInfoTest() {
        //given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();

        Faculty newTestedFaculty = testedFaculty;
        newTestedFaculty.setColor("NewTestColor");

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                host + port + fc + "/edit",
                HttpMethod.PUT,
                new HttpEntity<>(newTestedFaculty),
                new ParameterizedTypeReference<Faculty>() {
                }
        );

        ResponseEntity<Faculty> result2 = restTemplate.exchange(
                host + port + fc + "/get?id=" + newTestedFaculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertEquals(new ResponseEntity<>(newTestedFaculty,HttpStatus.OK), new ResponseEntity<>(result2.getBody(),result2.getStatusCode()));

//        delete test
        ResponseEntity<Faculty> studentResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + newTestedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Удаляет факультет")
    void deleteFacultyTest() {
        //given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();

//        when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
        ResponseEntity<Faculty> result = restTemplate.exchange(
                host + port + fc + "/get?id=" + testedFaculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

        //then
        assertEquals(HttpStatus.valueOf(200), facultyResponseEntity.getStatusCode());
        assertEquals(HttpStatus.valueOf(404), result.getStatusCode());
    }

    @Test
    @DisplayName("Возвращает все факультеты определённого цвета")
    void getFacultiesByColorTest() {
        //given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

        Faculty testFaculty2 = new Faculty();
        testFaculty2.setId(0L);
        testFaculty2.setName("TestFac2");
        testFaculty2.setColor("TestColor");

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty2,String.class))
                .isNotNull();

        Faculty testedFaculty2 = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac2"))
                .findFirst().orElseThrow();

        Collection<Faculty> faculties = new HashSet<>();
        faculties.add(testedFaculty);
        faculties.add(testedFaculty2);
        String color = testedFaculty.getColor();

//        when
        ResponseEntity<Collection<Faculty>> result = restTemplate.exchange(
                host + port + fc + "/getByColor?color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertArrayEquals(faculties.toArray(),result.getBody().toArray());

//        delete test
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
        ResponseEntity<Faculty> facultyResponseEntity2 = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty2.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Ищет факультет по названию")
    void findFacultyByNameTest() {
        //given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();


//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                host + port + fc + "/findByName?name=tEsTfAc",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertEquals(testedFaculty,result.getBody());

//        delete test
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Ищет факультет по цвету")
    void findFacultyByColorTest() {
        //given
        Faculty testFaculty = new Faculty();
        testFaculty.setId(0L);
        testFaculty.setName("TestFac");
        testFaculty.setColor("TestColor");

        Assertions
                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
                .isNotNull();

        Faculty testedFaculty = facultyController.getAllFaculties().stream()
                .filter(c -> c.getName().equals("TestFac"))
                .findFirst().orElseThrow();


//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                host + port + fc + "/findByColor?color=tEsTcOlOr",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertEquals(testedFaculty,result.getBody());

//        delete test
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                host + port + fc + "/delete?id=" + testedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );
    }

    @Test
    @DisplayName("Выводит студентов факультета")
    void getStudentsOfFacultyTest() {
//        //given
//        Faculty testFaculty = new Faculty();
//        testFaculty.setId(0L);
//        testFaculty.setName("TestFac");
//        testFaculty.setColor("TestColor");
//
//        Student testStudent = new Student();
//        testStudent.setId(0L);
//        testStudent.setName("Test");
//        testStudent.setAge(100);
//
//        Student testStudent2 = new Student();
//        testStudent2.setId(0L);
//        testStudent2.setName("Test2");
//        testStudent2.setAge(110);
//
//        Collection<Student> students = new HashSet<>();
//        students.add(testStudent);
//        students.add(testStudent2);
//        testFaculty.setStudentsOfFaculty(students);
//
//        Assertions
//                .assertThat(this.restTemplate.postForObject(host + port + fc + "/add",testFaculty,String.class))
//                .isNotNull();
//
//        Faculty testedFaculty = facultyController.getAllFaculties().stream()
//                .filter(c -> c.getName().equals("TestFac"))
//                .findFirst().orElseThrow();
//
////        when
//        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
//                host + port + fc + "/getStudentsOfFaculty?id=" + testedFaculty.getId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Collection<Student>>() {
//                }
//        );
//
////        then
//        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
//        assertArrayEquals(testedFaculty.getStudentsOfFaculty().toArray(), result.getBody().toArray());
//
////        delete test
//        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
//                host + port + fc + "/delete?id=" + testedFaculty.getId(),
//                HttpMethod.DELETE,
//                null,
//                new ParameterizedTypeReference<Faculty>() {
//                }
//        );
    }
}