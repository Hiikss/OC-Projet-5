package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setName("name");
        session.setDescription("description");
        session.setDate(new Date());
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        user1 = new User();
        user1.setId(1L);
        user2 = new User();
        user2.setId(2L);

        session.setUsers(Arrays.asList(user1, user2));

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("name");
        sessionDto.setDescription("description");
        sessionDto.setDate(new Date());
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
    }

    @Test
    void testToEntity() {
        // GIVEN
        teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // WHEN
        Session result = sessionMapper.toEntity(sessionDto);

        // THEN
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(teacher, result.getTeacher());
        assertEquals(Arrays.asList(user1, user2), result.getUsers());
    }

    @Test
    void testToEntity_NullDto() {
        // GIVEN

        // WHEN
        Session result = sessionMapper.toEntity((SessionDto) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToEntity_List() {
        // GIVEN
        teacher = new Teacher();
        session.setTeacher(teacher);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // WHEN
        List<Session> result = sessionMapper.toEntity(List.of(sessionDto));

        // THEN
        assertEquals(sessionDto.getId(), result.get(0).getId());
        assertEquals(sessionDto.getName(), result.get(0).getName());
        assertEquals(sessionDto.getDescription(), result.get(0).getDescription());
        assertEquals(sessionDto.getDate(), result.get(0).getDate());
        assertEquals(sessionDto.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.get(0).getUpdatedAt());
        assertEquals(teacher, result.get(0).getTeacher());
        assertEquals(Arrays.asList(user1, user2), result.get(0).getUsers());
    }

    @Test
    void testToEntity_NullList() {
        // GIVEN

        // WHEN
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto() {
        // GIVEN
        teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        // WHEN
        SessionDto result = sessionMapper.toDto(session);

        // THEN
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getDate(), result.getDate());
        assertEquals(session.getCreatedAt(), result.getCreatedAt());
        assertEquals(session.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(teacher.getId(), result.getTeacher_id());
        assertEquals(Arrays.asList(user1.getId(), user2.getId()), result.getUsers());
    }

    @Test
    void testToDto_NullEntity() {
        // GIVEN

        // WHEN
        SessionDto result = sessionMapper.toDto((Session) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto_List() {
        // GIVEN
        teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        // WHEN
        List<SessionDto> result = sessionMapper.toDto(List.of(session));

        // THEN
        assertEquals(session.getId(), result.get(0).getId());
        assertEquals(session.getName(), result.get(0).getName());
        assertEquals(session.getDescription(), result.get(0).getDescription());
        assertEquals(session.getDate(), result.get(0).getDate());
        assertEquals(session.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(session.getUpdatedAt(), result.get(0).getUpdatedAt());
        assertEquals(teacher.getId(), result.get(0).getTeacher_id());
        assertEquals(Arrays.asList(user1.getId(), user2.getId()), result.get(0).getUsers());
    }

    @Test
    void testToDto_NullList() {
        // GIVEN

        // WHEN
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

        // THEN
        assertNull(result);
    }

}

