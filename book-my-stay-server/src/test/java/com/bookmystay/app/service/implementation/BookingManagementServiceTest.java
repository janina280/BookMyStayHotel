package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.repository.BookingRepository;
import com.bookmystay.app.repository.RoomRepository;
import com.bookmystay.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookingManagementServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingManagementService bookingService;

    private Booking booking;
    private Room room;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a booking
        booking = new Booking();
        booking.setCheckInDate(LocalDate.of(2025, 11, 10));
        booking.setCheckOutDate(LocalDate.of(2025, 11, 12));

        // Initialize a room
        room = new Room();
        room.setId(1L);
        room.setBookings(new ArrayList<>());

        // Initialize a user
        user = new User();
        user.setId(1L);
    }

    // ----- saveBooking tests -----
    @Test
    void saveBooking_success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        assertNotNull(response.getBookingConfirmationCode());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void saveBooking_roomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
        assertEquals("Room not found", response.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_userNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found", response.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_roomNotAvailable() {
        Booking existingBooking = new Booking();
        existingBooking.setCheckInDate(LocalDate.of(2025, 11, 11));
        existingBooking.setCheckOutDate(LocalDate.of(2025, 11, 13));
        room.getBookings().add(existingBooking);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
        assertEquals("Room not available for selected date range", response.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void saveBooking_invalidDates() {
        booking.setCheckOutDate(LocalDate.of(2025, 11, 5)); // before checkIn

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
        assertEquals("Check-out date must come after check-in date", response.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    // ----- cancelBooking tests -----
    @Test
    void cancelBooking_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Response response = bookingService.cancelBooking(1L);

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void cancelBooking_bookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.cancelBooking(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("Booking not found", response.getMessage());
        verify(bookingRepository, never()).delete(any());
    }
}
