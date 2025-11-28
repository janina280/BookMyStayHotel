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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    // saveBooking tests
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

    //  cancelBooking tests
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


    @Test
    void updateBooking_success() {
        room.setBookings(new ArrayList<>());
        booking.setRoom(room);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Response response = bookingService.updateBooking(
                1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
        verify(bookingRepository, times(1)).save(booking);
    }


    @Test
    void updateBooking_roomUnavailable() {
        Booking existingBooking = new Booking();
        existingBooking.setId(2L);
        existingBooking.setCheckInDate(LocalDate.now().plusDays(2));
        existingBooking.setCheckOutDate(LocalDate.now().plusDays(4));

        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setId(1L);
        bookingToUpdate.setRoom(new Room());
        bookingToUpdate.getRoom().setBookings(List.of(existingBooking));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingToUpdate));

        Response response = bookingService.updateBooking(
                1L,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5)
        );

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getMessage().contains("Room is not available"));
    }

    @Test
    void updateBooking_bookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.updateBooking(1L, LocalDate.now(), LocalDate.now().plusDays(1));

        assertEquals(404, response.getStatusCode());
        assertEquals("Booking not found", response.getMessage());
    }

    @Test
    void updateBooking_invalidDates() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        booking.setRoom(room);

        Response response = bookingService.updateBooking(
                1L,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(3)
        );

        assertEquals(404, response.getStatusCode());
        assertEquals("Check-out date must be after check-in date", response.getMessage());

    }


}
