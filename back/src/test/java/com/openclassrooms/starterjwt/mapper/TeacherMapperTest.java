package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TeacherMapperTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testToEntity() {
        // GIVEN

        // WHEN
        Teacher result = teacherMapper.toEntity(teacherDto);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(teacherDto);
    }

    @Test
    void testToEntity_NullEntity() {
        // GIVEN

        // WHEN
        Teacher result = teacherMapper.toEntity((TeacherDto) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToEntity_List() {
        // GIVEN

        // WHEN
        List<Teacher> result = teacherMapper.toEntity(List.of(teacherDto));

        // THEN
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(teacherDto);
    }

    @Test
    void testToEntity_NullList() {
        // GIVEN

        // WHEN
        List<Teacher> result = teacherMapper.toEntity((List<TeacherDto>) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto() {
        // GIVEN

        // WHEN
        TeacherDto result = teacherMapper.toDto(teacher);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(teacher);
    }

    @Test
    void testToDto_NullEntity() {
        // GIVEN

        // WHEN
        TeacherDto result = teacherMapper.toDto((Teacher) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto_List() {
        // GIVEN

        // WHEN
        List<TeacherDto> result = teacherMapper.toDto(List.of(teacher));

        // THEN
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(teacher);
    }

    @Test
    void testToDto_NullList() {
        // GIVEN

        // WHEN
        List<TeacherDto> result = teacherMapper.toDto((List<Teacher>) null);

        // THEN
        assertNull(result);
    }
}

