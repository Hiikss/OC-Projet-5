package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void testFindAll() {
        // GIVEN
        List<Teacher> teachers = new ArrayList<>();
        when(this.teacherRepository.findAll()).thenReturn(teachers);

        // WHEN
        List<Teacher> result = teacherService.findAll();

        // THEN
        assertEquals(teachers, result);
        verify(teacherRepository).findAll();
    }

    @Test
    void testFindById_Success() {
        // GIVEN
        Long id = 1L;

        Teacher teacher = new Teacher();

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        // WHEN
        Teacher result = teacherService.findById(id);

        // THEN
        assertEquals(teacher, result);
        verify(teacherRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // GIVEN
        Long id = 1L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Teacher result = teacherService.findById(id);

        // THEN
        assertNull(result);
        verify(teacherRepository).findById(id);
    }
}
