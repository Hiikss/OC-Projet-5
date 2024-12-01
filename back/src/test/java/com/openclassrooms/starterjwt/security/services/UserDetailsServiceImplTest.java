package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_Success() {
        // GIVEN
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345")
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        // WHEN
        UserDetails result = userDetailsService.loadUserByUsername(user.getEmail());

        // THEN
        assertEquals(userDetails, result);
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void loadUserByUsername_NotFound() {
        // GIVEN
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@test.com"));
        verify(userRepository).findByEmail("test@test.com");
    }
}
