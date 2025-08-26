package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
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
class FacultyControllerTest {

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
        return "http://localhost:" + port + "/faculty" + end;
    }

    List<Faculty> fillRepositoryUp() {
        return Stream.generate(() -> facultyRepository.save(new Faculty()))
                .limit(nextInt(10,15))
                .toList();
    }

    Faculty getOneOfFaculties(List<Faculty> repository) {
        return repository.get(insecure().randomInt(0,repository.size()));
    }

    Faculty getTestFaculty(String name, String color) {
        Faculty test = new Faculty();
        test.setId(0);
        test.setName(name);
        test.setColor(color);
        return test;
    }

    @Test
    @DisplayName("Возвращает все факультеты")
    void getAllFacultiesTest() {
        //        given
        List<Faculty> expected = fillRepositoryUp();

//        when
        ResponseEntity<Collection<Faculty>> result = restTemplate.exchange(
                getURL("/getAll"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
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
    @DisplayName("Добавляет факультет")
    void addFacultyTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        Faculty expected = getTestFaculty("Test","Color");

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/add"),
                HttpMethod.POST,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<Faculty>() {
                }
        );

        expected.setId(result.getBody().getId());

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Возвращает факультет по id")
    void getFacultyTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        Faculty expected = getOneOfFaculties(repository);

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/get?id=")+expected.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Изменяет информацию о факультете")
    void editFacultyInfoTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        Faculty initial = getOneOfFaculties(repository);
        Faculty expected = new Faculty();
        expected.setId(initial.getId());
        expected.setName("Test");
        expected.setColor("Color");

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/edit"),
                HttpMethod.PUT,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
        assertTrue(facultyRepository.findAll().contains(expected));
        assertFalse(facultyRepository.findAll().contains(initial));
    }

    @Test
    @DisplayName("Удаляет факультет")
    void deleteFacultyTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        Faculty expected = getOneOfFaculties(repository);

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/delete?id=") + expected.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

        //then
        assertFalse(facultyRepository.findAll().contains(expected));
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
    }

//    @Test
//    @DisplayName("Возвращает все факультеты определённого цвета")
//    void getFacultiesByColorTest() {
////        given
//        List<Faculty> repository = fillRepositoryUp();
//        String color = "wserrdtfygu";
//        Faculty test = getTestFaculty("rtdf",color);
//        Faculty test2 = getTestFaculty("edtrfgyhj",color);
//        facultyRepository.save(test);
//        facultyRepository.save(test2);
//        List<Faculty> expected = new ArrayList<>();
//        expected.add(test);
//        expected.add(test2);
//
////        when
//        ResponseEntity<Collection<Faculty>> result = restTemplate.exchange(
//                getURL("/getByColor?color="+color),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Collection<Faculty>>() {
//                }
//        );
//
////        then
//        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
//        assertThat(result).isNotNull();
//        assertThat(result.getBody())
//                .isNotNull()
//                .isNotEmpty()
//                .containsExactlyInAnyOrderElementsOf(expected);
//    }

    @Test
    @DisplayName("Ищет факультет по названию")
    void findFacultyByNameTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        String name = "ezsrxdtrftgyhbj";
        Faculty expected = getTestFaculty(name, "color");
        facultyRepository.save(expected);

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/findByName?name=") + name.toUpperCase(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Ищет факультет по цвету")
    void findFacultyByColorTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        String color = "ezsrxdtrftgyhbj";
        Faculty expected = getTestFaculty("name", color);
        facultyRepository.save(expected);

//        when
        ResponseEntity<Faculty> result = restTemplate.exchange(
                getURL("/findByColor?color=") + color.toUpperCase(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

//        then
        assertEquals(HttpStatus.valueOf(200), result.getStatusCode());
        assertThat(result).isNotNull();
        assertThat(result.getBody()).isNotNull();
        assertEquals(expected,result.getBody());
    }

    @Test
    @DisplayName("Выводит студентов факультета")
    void getStudentsOfFacultyTest() {
//        given
        List<Faculty> repository = fillRepositoryUp();
        Faculty testFac = getOneOfFaculties(repository);
        Student test = new Student();
        test.setName("dxgfgcg");
        test.setAge(12);
        test.setFaculty(testFac);
        Student test2 = new Student();
        test2.setName("rerdf");
        test2.setAge(2);
        test2.setFaculty(testFac);
        studentRepository.save(test);
        studentRepository.save(test2);
        List<Student> expected = new ArrayList<>();
        expected.add(test);
        expected.add(test2);
        testFac.setStudentsOfFaculty(expected);
        facultyRepository.save(testFac);

//        when
        ResponseEntity<Collection<Student>> result = restTemplate.exchange(
                getURL("/getStudentsOfFaculty?id="+testFac.getId()),
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
}