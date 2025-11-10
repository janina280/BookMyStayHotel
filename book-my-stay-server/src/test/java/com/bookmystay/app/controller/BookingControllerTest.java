package com.bookmystay.app.controller;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.service.interfac.IBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private IBookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new Response();
        response.setStatusCode(200);
        response.setMessage("Success");
    }

    @Test
    void saveBookings_shouldReturnResponseFromService() {
        Booking bookingRequest = new Booking();
        when(bookingService.saveBooking(1L, 2L, bookingRequest)).thenReturn(response);

        ResponseEntity<Response> result = bookingController.saveBookings(1L, 2L, bookingRequest);

        verify(bookingService, times(1)).saveBooking(1L, 2L, bookingRequest);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getAllBookings_shouldReturnResponseFromService() {
        when(bookingService.getAllBookings()).thenReturn(response);

        ResponseEntity<Response> result = bookingController.getAllBookings();

        verify(bookingService, times(1)).getAllBookings();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void getBookingByConfirmationCode_shouldReturnResponseFromService() {
        when(bookingService.findBookingByConfirmationCode("ABC123")).thenReturn(response);

        ResponseEntity<Response> result = bookingController.getBookingByConfirmationCode("ABC123");

        verify(bookingService, times(1)).findBookingByConfirmationCode("ABC123");
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }

    @Test
    void cancelBooking_shouldReturnResponseFromService() {
        when(bookingService.cancelBooking(5L)).thenReturn(response);

        ResponseEntity<Response> result = bookingController.cancelBooking(5L);

        verify(bookingService, times(1)).cancelBooking(5L);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Success", result.getBody().getMessage());
    }
}
