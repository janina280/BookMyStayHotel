package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingQueryService queryService;

    @Mock
    private BookingManagementService managementService;

    @InjectMocks
    private BookingService bookingService;

    private Response mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockResponse = new Response();
        mockResponse.setMessage("OK");
    }

    @Test
    void testGetBookingById_delegatesToQueryService() {
        when(queryService.getBookingById("1")).thenReturn(mockResponse);

        Response result = bookingService.getBookingById("1");

        verify(queryService, times(1)).getBookingById("1");
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testFindBookingByConfirmationCode_delegatesToQueryService() {
        when(queryService.findBookingByConfirmationCode("ABC")).thenReturn(mockResponse);

        Response result = bookingService.findBookingByConfirmationCode("ABC");

        verify(queryService).findBookingByConfirmationCode("ABC");
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testGetAllBookings_delegatesToQueryService() {
        when(queryService.getAllBookings()).thenReturn(mockResponse);

        Response result = bookingService.getAllBookings();

        verify(queryService).getAllBookings();
        verifyNoInteractions(managementService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testSaveBooking_delegatesToManagementService() {
        Booking booking = new Booking();
        when(managementService.saveBooking(10L, 20L, booking)).thenReturn(mockResponse);

        Response result = bookingService.saveBooking(10L, 20L, booking);

        verify(managementService).saveBooking(10L, 20L, booking);
        verifyNoInteractions(queryService);
        assertEquals("OK", result.getMessage());
    }

    @Test
    void testCancelBooking_delegatesToManagementService() {
        when(managementService.cancelBooking(99L)).thenReturn(mockResponse);

        Response result = bookingService.cancelBooking(99L);

        verify(managementService).cancelBooking(99L);
        verifyNoInteractions(queryService);
        assertEquals("OK", result.getMessage());
    }
}
