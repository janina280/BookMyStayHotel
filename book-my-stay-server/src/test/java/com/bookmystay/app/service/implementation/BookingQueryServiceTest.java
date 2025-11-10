package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookingQueryServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingQueryService bookingQueryService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = new Booking();
        booking.setId(1L);
        booking.setBookingConfirmationCode("ABC123");
    }

    // ----- getBookingById tests -----
    @Test
    void getBookingById_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Response response = bookingQueryService.getBookingById("1");

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getBooking());
    }

    @Test
    void getBookingById_notFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingQueryService.getBookingById("1");

        assertEquals(404, response.getStatusCode());
        assertEquals("Booking not found", response.getMessage());
    }

    // ----- findBookingByConfirmationCode tests -----
    @Test
    void findBookingByConfirmationCode_success() {
        when(bookingRepository.findByBookingConfirmationCode("ABC123")).thenReturn(Optional.of(booking));

        Response response = bookingQueryService.findBookingByConfirmationCode("ABC123");

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getBooking());
    }

    @Test
    void findBookingByConfirmationCode_notFound() {
        when(bookingRepository.findByBookingConfirmationCode("ABC123")).thenReturn(Optional.empty());

        Response response = bookingQueryService.findBookingByConfirmationCode("ABC123");

        assertEquals(404, response.getStatusCode());
        assertEquals("Booking not found", response.getMessage());
    }

    // ----- getAllBookings tests -----
    @Test
    void getAllBookings_success() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(bookings);

        Response response = bookingQueryService.getAllBookings();

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getBookingList());
        assertEquals(1, response.getBookingList().size());
    }

    @Test
    void getAllBookings_emptyList() {
        when(bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(new ArrayList<>());

        Response response = bookingQueryService.getAllBookings();

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertTrue(response.getBookingList().isEmpty());
    }
}
