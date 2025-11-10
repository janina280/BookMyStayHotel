package com.bookmystay.app.controller;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.service.interfac.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new Response();
        response.setStatusCode(200);
        response.setMessage("Success");
    }

    @Test
    void getAllUsers_shouldReturnResponseFromService() {
        when(userService.getAllUsers()).thenReturn(response);

        ResponseEntity<Response> result = userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getUserById_shouldReturnResponseFromService() {
        when(userService.getUserById("1")).thenReturn(response);

        ResponseEntity<Response> result = userController.getUserById("1");

        verify(userService, times(1)).getUserById("1");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void delete_shouldReturnResponseFromService() {
        when(userService.deleteUser("1")).thenReturn(response);

        ResponseEntity<Response> result = userController.delete("1");

        verify(userService, times(1)).deleteUser("1");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getLoggedInProfileInfo_shouldReturnResponseFromService() {
        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("test@example.com");
        when(userService.getMyInfo("test@example.com")).thenReturn(response);

        ResponseEntity<Response> result = userController.getLoggedInProfileInfo();

        verify(userService, times(1)).getMyInfo("test@example.com");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getUserBookingHistory_shouldReturnResponseFromService() {
        when(userService.getUserBookingHistory("1")).thenReturn(response);

        ResponseEntity<Response> result = userController.getUserBookingHistory("1");

        verify(userService, times(1)).getUserBookingHistory("1");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }
}
