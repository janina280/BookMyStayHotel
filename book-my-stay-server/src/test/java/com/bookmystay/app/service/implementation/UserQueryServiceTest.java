package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.repository.UserRepository;
import com.bookmystay.app.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- getAllUsers tests -----
    @Test
    void getAllUsers_success() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.mapUserListEntityToUserDTO(List.of(user))).thenReturn(List.of());

            Response response = userQueryService.getAllUsers();

            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getUserList());
            verify(userRepository, times(1)).findAll();
        }
    }

    @Test
    void getAllUsers_exception() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        Response response = userQueryService.getAllUsers();

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error fetching users"));
    }

    // ----- getUserById tests -----


    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = userQueryService.getUserById(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());
    }

    @Test
    void getUserById_exception() {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        Response response = userQueryService.getUserById(1L);

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error fetching user"));
    }

    @Test
    void getUserBookingHistory_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = userQueryService.getUserBookingHistory(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());
    }

    // ----- deleteUser tests -----
    @Test
    void deleteUser_success() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Response response = userQueryService.deleteUser(1L);

        assertEquals(200, response.getStatusCode());
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = userQueryService.deleteUser(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());
    }

    @Test
    void getMyInfo_notFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Response response = userQueryService.getMyInfo("test@example.com");

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());
    }
}
