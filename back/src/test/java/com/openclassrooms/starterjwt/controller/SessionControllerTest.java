package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SessionController.class)
@ContextConfiguration(classes = {SessionController.class})
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Test
    @DisplayName("GET /api/session/{id} - Success")
    void testFindById_Success() throws Exception {
        // GIVEN
        Session session = new Session();

        SessionDto sessionDto = new SessionDto();

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(sessionDto)));
    }

    @Test
    @DisplayName("GET /api/session/{id} - Not Found")
    void testFindById_NotFound() throws Exception {
        // GIVEN
        when(sessionService.getById(1L)).thenReturn(null);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/session/{id} - Bad Request")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/session - Success")
    void testFindAll_Success() throws Exception {
        // GIVEN
        Session session1 = new Session();
        Session session2 = new Session();
        List<Session> sessions = List.of(session1, session2);
        List<SessionDto> sessionDtos = List.of(new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(sessionDtos)));
    }

    @Test
    @DisplayName("POST /api/session - Create Session Success")
    void testCreateSession_Success() throws Exception {
        // GIVEN
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("test");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("description");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // WHEN
        // THEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(sessionDto)));
    }

    @Test
    @DisplayName("PUT /api/session/{id} - Update Session Success")
    void testUpdateSession_Success() throws Exception {
        // GIVEN
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("test");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("description");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // WHEN
        // THEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(sessionDto)));
    }

    @Test
    @DisplayName("PUT /api/session/{id} - Bad Request")
    void testUpdateSession_BadRequest() throws Exception {
        // GIVEN
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("test");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("description");

        when(sessionService.update(eq(1L), any())).thenThrow(NumberFormatException.class);

        // WHEN
        // THEN
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/session/{id} - Delete Session Success")
    void testDeleteSession_Success() throws Exception {
        // GIVEN
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/session/{id} - Bad Request")
    void testDeleteSession_BadRequest() throws Exception {
        // GIVEN
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);
        doThrow(new NumberFormatException()).when(sessionService).delete(1L);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/session/{id} - Not Found")
    void testDeleteSession_NotFound() throws Exception {
        // GIVEN
        when(sessionService.getById(1L)).thenReturn(null);

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/session/{id}/participate/{userId} - Participate in Session Success")
    void testParticipateInSession_Success() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/session/{id}/participate/{userId} - No Longer Participate Success")
    void testNoLongerParticipateInSession_Success() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/session/{id}/participate/{userId} - Bad Request")
    void testParticipateInSession_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(post("/api/session/invalid-id/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/session/{id}/participate/{userId} - Bad Request")
    void testNoLongerParticipateInSession_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(delete("/api/session/invalid-id/participate/2"))
                .andExpect(status().isBadRequest());
    }
}
