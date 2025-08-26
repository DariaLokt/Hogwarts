package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    @DisplayName("Добавляет ученика")
    public void addStudentTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Jill";
        int age = 10;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id",id);
        studentObject.put("name",name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

//        when
        when(studentRepository.save(any(Student.class))).thenReturn(student);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student/add")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
        verify(studentService,only()).addStudent(student);
        verify(studentRepository,only()).save(student);
    }

    @Test
    @DisplayName("Выводит ученика по ID")
    public void getStudentTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Jill";
        int age = 10;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

//        when
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get")
                        .param("id",String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
        verify(studentService,only()).getStudent(id);
        verify(studentRepository,only()).findById(id);
    }

    @Test
    @DisplayName("Выводит всех учеников")
    public void getAllStudentsTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Jill";
        int age = 10;
        Long id2 = 2L;
        String name2 = "Lily";
        int age2 = 10;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student2);

//        when
        when(studentRepository.findAll()).thenReturn(list);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].id").value(id))
                .andExpect(jsonPath("$[1].name").value(name))
                .andExpect(jsonPath("$[1].age").value(age));
        verify(studentService,only()).getAll();
        verify(studentRepository,only()).findAll();
    }

    @Test
    @DisplayName("Меняет информацию ученика")
    public void editStudentInfoTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Jill";
        int age = 10;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id",id);
        studentObject.put("name",name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

//        when
        when(studentRepository.save(any(Student.class))).thenReturn(student);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/edit")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
        verify(studentService,only()).editStudentInformation(student);
        verify(studentRepository,only()).save(student);
    }

    @Test
    @DisplayName("Удаляет ученика")
    public void deleteStudentTest() throws Exception {
//        given
        Long id = 1L;

//        when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/delete")
                        .param("id",String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

//        then
        verify(studentService,only()).deleteStudent(id);
        verify(studentRepository,only()).deleteById(id);
    }

    @Test
    @DisplayName("Ищет учеников определённого возраста")
    public void getStudentsByAgeTest() throws Exception {
        //        given
        Long id = 1L;
        String name = "Jill";
        int age = 10;
        Long id2 = 2L;
        String name2 = "Lily";
        int age2 = 10;
        Long id3 = 3L;
        String name3 = "Jane";
        int age3 = 12;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student2);
        list.add(student3);

//        when
        when(studentRepository.findAll()).thenReturn(list);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/getByAge")
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].id").value(id))
                .andExpect(jsonPath("$[1].name").value(name))
                .andExpect(jsonPath("$[1].age").value(age));
        verify(studentService,only()).getByAge(age);
        verify(studentRepository,only()).findAll();
    }

    @Test
    @DisplayName("Ищет учеников в возрасте от-до")
    public void findStudentsByAgeGapTest() throws Exception {
//      given
        Long id = 1L;
        String name = "Jill";
        int age = 10;
        Long id2 = 2L;
        String name2 = "Lily";
        int age2 = 9;
        Long id3 = 3L;
        String name3 = "Jane";
        int age3 = 12;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student2);
//        list.add(student3);

//        when
        when(studentRepository.findByAgeBetween(anyInt(),anyInt())).thenReturn(list);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/findByAgeBetween")
                        .param("min", "9")
                        .param("max","11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
        verify(studentService,only()).findByAgeBetween(9,11);
        verify(studentRepository,only()).findByAgeBetween(9,11);
    }

    @Test
    @DisplayName("Выводит факультет студента")
    public void getFacultyTest() throws Exception {
//      given
        Long id = 1L;
        String name = "Jill";
        int age = 10;
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Name");
        faculty.setColor("Color");

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);


//        when
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/getFaculty")
                        .param("id", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.color").value("Color"));
        verify(studentService,only()).getFaculty(id);
        verify(studentRepository,only()).findById(id);
    }
}

