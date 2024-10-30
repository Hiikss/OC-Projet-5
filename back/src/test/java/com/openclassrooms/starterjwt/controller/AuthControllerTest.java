package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("POST /api/auth/login - Successful Authentication")
    void testAuthenticateUser_Success() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@example.com", "John", "Doe", false, "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        User user = new User("user@example.com", "Doe", "John", "encodedPassword", false);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        // WHEN
        // THEN
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(
                                new JwtResponse("jwt-token", 1L, user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin()))
                ));
    }

    @Test
    @DisplayName("POST /api/auth/login - No User")
    void testAuthenticateUser_NoUser() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@example.com", "John", "Doe", false, "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        User user = new User("user@example.com", "Doe", "John", "encodedPassword", false);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());

        // WHEN
        // THEN
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        new ObjectMapper().writeValueAsString(
                                new JwtResponse("jwt-token", 1L, user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin()))
                ));
    }

    @Test
    @DisplayName("POST /api/auth/register - Successful Registration")
    void testRegisterUser_Success() throws Exception {
        // GIVEN
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // WHEN
        // THEN
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(new MessageResponse("User registered successfully!"))));
    }

    @Test
    @DisplayName("POST /api/auth/register - Email Already Taken")
    void testRegisterUser_EmailTaken() throws Exception {
        // GIVEN
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // WHEN
        // THEN
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(new MessageResponse("Error: Email is already taken!"))));
    }
}
