package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testDelete() {
        // GIVEN
        Long id = 1L;

        // WHEN
        userService.delete(id);

        // THEN
        verify(userRepository).deleteById(id);
    }

    @Test
    void testFindById_Success() {
        // GIVEN
        Long id = 1L;

        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // WHEN
        User result = userService.findById(id);

        // THEN
        assertEquals(result, user);
        verify(userRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // GIVEN
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        User result = userService.findById(id);

        // THEN
        assertNull(result);
        verify(userRepository).findById(id);
    }
}
