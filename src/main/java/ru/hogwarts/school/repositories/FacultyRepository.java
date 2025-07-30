package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {

    Faculty findByNameIgnoreCase(String name);

    Faculty findByColorIgnoreCase(String color);
}
