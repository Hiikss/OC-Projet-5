package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeacherController.class)
@ContextConfiguration(classes = {TeacherController.class})
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private TeacherService teacherService;

    @Test
    @DisplayName("GET /api/teacher/{id} - Success")
    void testFindById_Success() throws Exception {
        // GIVEN
        Teacher teacher = new Teacher();
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());

        // WHEN
        // THEN
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(teacher)));
    }

    @Test
    @DisplayName("GET /api/teacher/{id} - Not Found")
    void testFindById_NotFound() throws Exception {
        // GIVEN
        when(teacherService.findById(1L)).thenReturn(null);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/teacher/{id} - Bad Request")
    void testFindById_BadRequest() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get("/api/teacher/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/teacher - Success")
    void testFindAll() throws Exception {
        // GIVEN
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = List.of(teacher1, teacher2);

        TeacherDto dto1 = new TeacherDto();
        TeacherDto dto2 = new TeacherDto();
        List<TeacherDto> teacherDtos = List.of(dto1, dto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // WHEN
        // THEN
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(teacherDtos)));
    }
}
