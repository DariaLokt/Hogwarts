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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyControllerTest {
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
    @DisplayName("Добавляет факультет")
    public void addFacultyTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id",id);
        facultyObject.put("name",name);
        facultyObject.put("color",color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

//        when
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty/add")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyService,only()).addFaculty(faculty);
        verify(facultyRepository,only()).save(faculty);
    }

    @Test
    @DisplayName("Выводит факультет по ID")
    public void getFacultyTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

//        when
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/get")
                        .param("id",String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyService,only()).getFaculty(id);
        verify(facultyRepository,only()).findById(id);
    }

    @Test
    @DisplayName("Выводит все факультеты")
    public void getAllFacultiesTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";
        Long id2 = 2L;
        String name2 = "Test name 2";
        String color2 = "Test color 2";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        List<Faculty> list = new ArrayList<>();
        list.add(faculty);
        list.add(faculty2);

//        when
        when(facultyRepository.findAll()).thenReturn(list);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
        verify(facultyService,only()).getAll();
        verify(facultyRepository,only()).findAll();
    }

    @Test
    @DisplayName("Меняет информацию о факультете")
    public void editFacultyInfoTest() throws Exception {
//        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id",id);
        facultyObject.put("name",name);
        facultyObject.put("color",color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

//        when
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/edit")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyService,only()).editFacultyInformation(faculty);
        verify(facultyRepository,only()).save(faculty);
    }

    @Test
    @DisplayName("Удаляет факультет")
    public void deleteFacultyTest() throws Exception {
//        given
        Long id = 1L;

//        when
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/delete")
                        .param("id",String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

//        then
        verify(facultyService,only()).deleteFaculty(id);
        verify(facultyRepository,only()).deleteById(id);
    }

    @Test
    @DisplayName("Ищет факультет по цвету")
    public void getFacultiesByColorTest() throws Exception {
        //        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";
        Long id2 = 2L;
        String name2 = "Test name 2";
        String color2 = "Test color 2";
        Long id3 = 3L;
        String name3 = "Test name 3";
        String color3 = "Test color 3";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(id3);
        faculty3.setName(name3);
        faculty3.setColor(color);

        List<Faculty> list = new ArrayList<>();
        list.add(faculty);
        list.add(faculty2);
        list.add(faculty3);

//        when
        when(facultyRepository.findAll()).thenReturn(list);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/getByColor")
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
        verify(facultyService,only()).getByColor(color);
        verify(facultyRepository,only()).findAll();
    }

    @Test
    @DisplayName("Выводит всех студентов факультета")
    public void getStudentsOfFacultyTest() throws Exception {
        //        given
        Long id = 1L;
        String name = "Test name";
        String color = "Test color";
        Long id1 = 1L;
        String name1 = "Jill";
        int age1 = 10;
        Long id2 = 2L;
        String name2 = "Lily";
        int age2 = 10;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Collection<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setStudentsOfFaculty(students);

//        when
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/getStudentsOfFaculty")
                        .param("id", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].age").value(age1));
        verify(facultyService,only()).getFacultyStudents(id);
        verify(facultyRepository,only()).findById(id);
    }

    @Test
    @DisplayName("Выводит факультет по цвету, игнорируя раскладку")
    public void findFacultyByColorTest() throws Exception {
        //        given
        Long id = 1L;
        String name = "name";
        String color = "color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

//        when
        when(facultyRepository.findByColorIgnoreCase(anyString())).thenReturn(faculty);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByColor")
                        .param("color", color.toUpperCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyService,only()).findByColor(color.toUpperCase());
        verify(facultyRepository,only()).findByColorIgnoreCase(color.toUpperCase());
    }

    @Test
    @DisplayName("Выводит факультет по названию, игнорируя раскладку")
    public void findFacultyByNameTest() throws Exception {
//      given
        Long id = 1L;
        String name = "name";
        String color = "color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

//        when
        when(facultyRepository.findByNameIgnoreCase(anyString())).thenReturn(faculty);

//        then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findByName")
                        .param("name", name.toUpperCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyService,only()).findByName(name.toUpperCase());
        verify(facultyRepository,only()).findByNameIgnoreCase(name.toUpperCase());
    }
}