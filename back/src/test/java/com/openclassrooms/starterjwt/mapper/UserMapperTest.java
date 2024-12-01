package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
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
class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@gmail.com");
        userDto.setPassword("password");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testToEntity() {
        // GIVEN

        // WHEN
        User result = userMapper.toEntity(userDto);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(userDto);
    }

    @Test
    void testToEntity_NullDto() {
        // GIVEN

        // WHEN
        User result = userMapper.toEntity((UserDto) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToEntity_List() {
        // GIVEN

        // WHEN
        List<User> result = userMapper.toEntity(List.of(userDto));

        // THEN
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(userDto);
    }

    @Test
    void testToEntity_NullList() {
        // GIVEN

        // WHEN
        List<User> result = userMapper.toEntity((List<UserDto>) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto() {
        // GIVEN

        // WHEN
        UserDto result = userMapper.toDto(user);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void testToDto_NullEntity() {
        // GIVEN

        // WHEN
        UserDto result = userMapper.toDto((User) null);

        // THEN
        assertNull(result);
    }

    @Test
    void testToDto_List() {
        // GIVEN

        // WHEN
        List<UserDto> result = userMapper.toDto(List.of(user));

        // THEN
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void testToDto_NullList() {
        // GIVEN

        // WHEN
        List<UserDto> result = userMapper.toDto((List<User>) null);

        // THEN
        assertNull(result);
    }
}

