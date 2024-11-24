package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById() throws Exception {
        // GIVEN
        User user = new User();
        user.setEmail("john.doe@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())));
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_NotFound() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteById() throws Exception {
        // GIVEN
        User user = new User();
        user.setEmail("john.doe@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteUser_Unauthorized() throws Exception {
        // GIVEN
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteById_NotFound() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}