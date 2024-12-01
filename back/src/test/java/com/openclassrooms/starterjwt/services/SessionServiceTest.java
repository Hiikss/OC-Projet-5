package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testCreate() {
        // GIVEN
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // WHEN
        Session result = sessionService.create(session);

        // THEN
        assertEquals(session, result);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void testDelete() {
        // GIVEN
        Long sessionId = 1L;

        // WHEN
        sessionService.delete(sessionId);

        // THEN
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    void testFindAll() {
        // GIVEN
        List<Session> sessions = new ArrayList<>();
        when(sessionRepository.findAll()).thenReturn(sessions);

        // WHEN
        List<Session> result = sessionService.findAll();

        // THEN
        assertEquals(sessions, result);
        verify(sessionRepository).findAll();
    }

    @Test
    void testGetById_Success() {
        // GIVEN
        Long sessionId = 1L;
        Session session = new Session();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // WHEN
        Session returnedSession = sessionService.getById(sessionId);

        // THEN
        assertEquals(session, returnedSession);
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void testGetById_NotFound() {
        // GIVEN
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // WHEN
        Session returnedSession = sessionService.getById(sessionId);

        // THEN
        assertNull(returnedSession);
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void testUpdate() {
        // GIVEN
        Long sessionId = 1L;
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        // WHEN
        Session result = sessionService.update(sessionId, session);

        // THEN
        assertEquals(session, result);
        assertEquals(sessionId, result.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    void testParticipate_Success() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // WHEN
        // THEN
        assertDoesNotThrow(() -> sessionService.participate(sessionId, userId));
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testParticipate_SessionNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testParticipate_UserNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testParticipate_AlreadyParticipating() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(List.of(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // WHEN
        // THEN
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testNoLongerParticipate_Success() {
        // WHEN
        Long sessionId = 1L;
        Long userId = 2L;

        User user1 = new User();
        user1.setId(userId);

        User user2 = new User();
        user2.setId(3L);

        Session session = new Session();
        session.setId(sessionId);
        List<User> users = List.of(user1, user2);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // WHEN
        // THEN
        assertDoesNotThrow(() -> sessionService.noLongerParticipate(sessionId, userId));
        assertFalse(session.getUsers().contains(user1));
        verify(sessionRepository).save(session);
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testNoLongerParticipate_NotParticipating() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // WHEN
        // THEN
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
        verify(sessionRepository, never()).save(any());
    }
}
