package com.bnppf.kata.online_book_store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bnppf.kata.online_book_store.dto.LoginRequest;
import com.bnppf.kata.online_book_store.dto.RegisterRequest;
import com.bnppf.kata.online_book_store.dto.UserResponse;
import com.bnppf.kata.online_book_store.entity.User;
import com.bnppf.kata.online_book_store.exception.InvalidCredentialsException;
import com.bnppf.kata.online_book_store.exception.UserAlreadyExistsException;
import com.bnppf.kata.online_book_store.repository.UserRepository;
import com.bnppf.kata.online_book_store.utility.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // Mocked now

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // No manual instantiation, @InjectMocks handles constructor injection
    }

    @Test
    void register_shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        User userEntity = new User();
        when(userMapper.registerRequestToUser(request)).thenReturn(userEntity);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword"); // Will be overridden
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = new UserResponse(1L, "john", "john@example.com");
        when(userMapper.userToUserResponse(savedUser)).thenReturn(response);

        UserResponse result = userService.register(request);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository).existsByUsername("john");
        verify(userRepository).existsByEmail("john@example.com");
        verify(userMapper).registerRequestToUser(request);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserResponse(savedUser);
    }

    @Test
    void register_shouldThrowWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(request);
        });

        assertEquals("Username is already taken", ex.getMessage());

        verify(userRepository).existsByUsername("john");
        verify(userRepository, never()).existsByEmail(any());
        verify(userMapper, never()).registerRequestToUser(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowWhenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(request);
        });

        assertEquals("Email is already in use", ex.getMessage());

        verify(userRepository).existsByUsername("john");
        verify(userRepository).existsByEmail("john@example.com");
        verify(userMapper, never()).registerRequestToUser(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success_returnsUserResponse() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        UserResponse expectedResponse = new UserResponse(1L, "john", "john@example.com");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(userMapper.userToUserResponse(user)).thenReturn(expectedResponse);

        // Act
        UserResponse actualResponse = userService.login(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
    }

    @Test
    void login_userNotFound_throwsInvalidCredentialsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("invalidUser");
        request.setPassword("password123");

        when(userRepository.findByUsername("invalidUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void login_passwordDoesNotMatch_throwsInvalidCredentialsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }
}
