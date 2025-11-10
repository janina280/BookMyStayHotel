package com.bookmystay.app.utils;

import com.bookmystay.app.dto.BookingDTO;
import com.bookmystay.app.dto.RoomDTO;
import com.bookmystay.app.dto.UserDTO;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testGenerateRandomConfirmationCode_validLengthAndCharacters() {
        int length = 12;
        String code = Utils.generateRandomConfirmationCode(length);

        assertNotNull(code);
        assertEquals(length, code.length());
        assertTrue(code.matches("[A-Z0-9]+"));
    }

    @Test
    void testMapUserEntityToUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPhoneNumber("123456789");
        user.setRole("USER");

        UserDTO dto = Utils.mapUserEntityToUserDTO(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(user.getRole(), dto.getRole());
    }

    @Test
    void testMapUserEntityToUserDTOPlusUserBookingsAndRoom_withBookings() {
        Room room = new Room();
        room.setId(10L);
        room.setRoomType("Deluxe");

        Booking booking = new Booking();
        booking.setId(100L);
        booking.setBookingConfirmationCode("ABC123");
        booking.setRoom(room);

        User user = new User();
        user.setId(1L);
        user.setName("Jane");
        user.setEmail("jane@example.com");
        user.setBookings(List.of(booking));

        UserDTO dto = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(1, dto.getBookings().size());
        assertEquals("ABC123", dto.getBookings().get(0).getBookingConfirmationCode());
        assertEquals("Deluxe", dto.getBookings().get(0).getRoom().getRoomType());
    }

    @Test
    void testMapRoomEntityToRoomDTO() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomDescription("Nice room");
        room.setRoomType("Suite");
        room.setRoomPrice(BigDecimal.valueOf(250));
        room.setRoomPhotoUrl("url");

        RoomDTO dto = Utils.mapRoomEntityToRoomDTO(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomDescription(), dto.getRoomDescription());
        assertEquals(room.getRoomType(), dto.getRoomType());
        assertEquals(room.getRoomPrice(), dto.getRoomPrice());
        assertEquals(room.getRoomPhotoUrl(), dto.getRoomPhotoUrl());
    }

    @Test
    void testMapRoomEntityToRoomDTOPlusBookings() {
        Booking booking = new Booking();
        booking.setId(5L);
        booking.setBookingConfirmationCode("CONF123");

        Room room = new Room();
        room.setId(2L);
        room.setRoomType("Economy");
        room.setBookings(List.of(booking));

        RoomDTO dto = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(1, dto.getBookings().size());
        assertEquals("CONF123", dto.getBookings().get(0).getBookingConfirmationCode());
    }

    @Test
    void testMapBookingEntityToBookingDTO() {
        Booking booking = new Booking();
        booking.setId(3L);
        booking.setBookingConfirmationCode("CODE321");
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(1);
        booking.setTotalNumOfGuest(3);
        booking.setCheckInDate(LocalDate.of(2025, 1, 10));
        booking.setCheckOutDate(LocalDate.of(2025, 1, 12));

        BookingDTO dto = Utils.mapBookingEntityToBookingDTO(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getBookingConfirmationCode(), dto.getBookingConfirmationCode());
        assertEquals(booking.getNumOfAdults(), dto.getNumOfAdults());
        assertEquals(booking.getNumOfChildren(), dto.getNumOfChildren());
        assertEquals(booking.getTotalNumOfGuest(), dto.getTotalNumOfGuest());
        assertEquals(booking.getCheckInDate(), dto.getCheckInDate());
        assertEquals(booking.getCheckOutDate(), dto.getCheckOutDate());
    }

    @Test
    void testMapBookingEntityToBookingDTOPlusBookedRooms_withRoomAndUser() {
        User user = new User();
        user.setId(7L);
        user.setName("Maria");

        Room room = new Room();
        room.setId(8L);
        room.setRoomType("Premium");

        Booking booking = new Booking();
        booking.setId(9L);
        booking.setUser(user);
        booking.setRoom(room);
        booking.setBookingConfirmationCode("XYZ789");

        BookingDTO dto = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);

        assertEquals(booking.getId(), dto.getId());
        assertNotNull(dto.getUser());
        assertEquals("Maria", dto.getUser().getName());
        assertEquals("Premium", dto.getRoom().getRoomType());
    }

    @Test
    void testMapUserListEntityToUserDTO() {
        User u1 = new User();
        u1.setId(1L);
        u1.setName("A");

        User u2 = new User();
        u2.setId(2L);
        u2.setName("B");

        List<UserDTO> result = Utils.mapUserListEntityToUserDTO(List.of(u1, u2));

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
    }

    @Test
    void testMapRoomListEntityToRoomDTO() {
        Room r1 = new Room();
        r1.setId(1L);
        r1.setRoomType("Single");

        Room r2 = new Room();
        r2.setId(2L);
        r2.setRoomType("Double");

        List<RoomDTO> result = Utils.mapRoomListEntityToRoomDTO(List.of(r1, r2));

        assertEquals(2, result.size());
        assertEquals("Single", result.get(0).getRoomType());
        assertEquals("Double", result.get(1).getRoomType());
    }

    @Test
    void testMapBookingListEntityToBookingDTO() {
        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setBookingConfirmationCode("A");

        Booking b2 = new Booking();
        b2.setId(2L);
        b2.setBookingConfirmationCode("B");

        List<BookingDTO> result = Utils.mapBookingListEntityToBookingDTO(List.of(b1, b2));

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getBookingConfirmationCode());
        assertEquals("B", result.get(1).getBookingConfirmationCode());
    }
}
