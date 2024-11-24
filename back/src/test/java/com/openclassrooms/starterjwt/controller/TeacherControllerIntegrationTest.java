package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockmMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById() throws Exception {
        // GIVEN
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        // WHEN
        // THEN
        mockmMvc.perform(get("/api/teacher/" + teacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(teacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teacher.getLastName())));
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_NotFound() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockmMvc.perform(get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockmMvc.perform(get("/api/teacher/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindAll() throws Exception {
        // GIVEN
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        teacherRepository.save(teacher);

        // WHEN
        // THEN
        mockmMvc.perform(get("/api/teacher/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.firstName == '%s' && @.lastName == '%s')]", "John", "Doe").exists());
    }
}