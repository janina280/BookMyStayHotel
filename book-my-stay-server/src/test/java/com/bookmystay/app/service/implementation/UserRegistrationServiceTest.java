package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_emailAlreadyExists() {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        Response response = registrationService.register(user);

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getMessage().contains("already exists"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_repositoryThrowsException() {
        User user = new User();
        user.setEmail("fail@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail("fail@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB error"));

        Response response = registrationService.register(user);

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error during registration"));
    }
}
