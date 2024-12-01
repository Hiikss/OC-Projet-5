package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById() throws Exception {
        // GIVEN
        Session session = new Session();
        session.setName("name");
        session.setDate(new Date());
        session.setDescription("description");
        sessionRepository.save(session);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(session.getName())));
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_NotFound() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindAll() throws Exception {
        // GIVEN
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setName("testName");
        session.setDate(new Date());
        session.setDescription("testDescription");
        sessionRepository.save(session);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == '%s')]", "testName").exists())
                .andExpect(jsonPath("$[?(@.description == '%s')]", "testDescription").exists());

    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testCreate() throws Exception {
        // GIVEN
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("testName");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("testDescription");

        // WHEN
        // THEN
        mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sessionDto.getName())))
                .andExpect(jsonPath("$.description", is(sessionDto.getDescription())));
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testUpdate() throws Exception {
        // GIVEN
        Session session = new Session();
        session.setName("testName");
        session.setDate(new Date());
        session.setDescription("testDescription");
        sessionRepository.save(session);

        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("nameUpdated");
        updatedSessionDto.setDate(new Date());
        updatedSessionDto.setTeacher_id(teacher.getId());
        updatedSessionDto.setDescription("descriptionUpdated");

        // WHEN
        // THEN
        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedSessionDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedSessionDto.getDescription())));
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testDelete() throws Exception {
        // GIVEN
        Session session = new Session();
        session.setName("testName");
        session.setDate(new Date());
        session.setDescription("testDescription");
        sessionRepository.save(session);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testParticipate() throws Exception {
        // GIVEN
        Session session = new Session();
        session.setName("testName");
        session.setDate(new Date());
        session.setDescription("testDescription");
        sessionRepository.save(session);

        User user = new User();
        user.setLastName("John");
        user.setFirstName("Doe");
        user.setEmail("john.doe@test.com");
        user.setPassword("password");
        user.setAdmin(false);
        userRepository.save(user);

        // WHEN
        // THEN
        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testNoLongerParticipate() throws Exception {
        // GIVEN
        User user = new User();
        user.setLastName("John");
        user.setFirstName("Doe");
        user.setEmail("john.doe@test.com");
        user.setPassword("password");
        user.setAdmin(false);
        userRepository.save(user);

        Session session = new Session();
        session.setName("testName");
        session.setDate(new Date());
        session.setDescription("Description");
        session.setUsers(new ArrayList<>());


        session.getUsers().add(user);
        sessionRepository.save(session);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}