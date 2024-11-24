package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @DisplayName("GET /api/user/{id} - Success")
    void testFindById_Success() throws Exception {
        // GIVEN
        User user = new User();

        UserDto userDto = new UserDto();
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDto)));
    }

    @Test
    @DisplayName("GET /api/user/{id} - Not Found")
    void testFindById_NotFound() throws Exception {
        // GIVEN
        when(userService.findById(1L)).thenReturn(null);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/user/{id} - Bad Request")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Success")
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteUser_Success() throws Exception {
        // GIVEN
        User user = User.builder()
                .id(1L)
                .email("john.doe@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("passsword")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        when(userService.findById(1L)).thenReturn(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("john.doe@test.com")
                .password("password")
                .roles("USER")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Unauthorized")
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteUser_Unauthorized() throws Exception {
        // GIVEN
        User user = new User();
        when(userService.findById(1L)).thenReturn(user);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Not Found")
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteUser_NotFound() throws Exception {
        // GIVEN
        when(userService.findById(1L)).thenReturn(null);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/user/{id} - Bad Request")
    @WithMockUser(username = "john.doe@test.com")
    void testDeleteUser_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }
}
