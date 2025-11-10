package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.entity.Room;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.BookingRepository;
import com.bookmystay.app.repository.RoomRepository;
import com.bookmystay.app.repository.UserRepository;
import com.bookmystay.app.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class BookingManagementService {

    private static final String BOOKING_NOT_FOUND = "Booking not found";
    private static final String ROOM_NOT_FOUND = "Room not found";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String SUCCESS_MESSAGE = "successful";
    private static final String ERROR_CONTEXT = "Error managing booking: ";

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingManagementService(BookingRepository bookingRepository,
                                    RoomRepository roomRepository,
                                    UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @FunctionalInterface
    private interface BookingManagementCall {
        void execute(Response response) throws OurException;
    }

    private Response handle(BookingManagementCall operation) {
        Response response = new Response();
        try {
            operation.execute(response);
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            response.setStatusCode(500);
            response.setMessage(ERROR_CONTEXT + e.getMessage());
        }
        return response;
    }


    public Response saveBooking(Long roomId, Long userId, Booking booking) {
        return handle(response -> {
            validateBookingDates(booking);
            Room room = getRoomOrThrow(roomId);
            User user = getUserOrThrow(userId);

            if (!isRoomAvailable(booking, room)) {
                throw new OurException("Room not available for selected date range");
            }

            assignBookingDetails(booking, room, user);
            bookingRepository.save(booking);

            response.setBookingConfirmationCode(booking.getBookingConfirmationCode());
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        });
    }

    public Response cancelBooking(Long bookingId) {
        return handle(response -> {
            Booking booking = getBookingOrThrow(bookingId);
            bookingRepository.delete(booking);
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        });
    }

    // ----- PRIVATE HELPERS -----
    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new OurException(BOOKING_NOT_FOUND));
    }

    private Room getRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new OurException(ROOM_NOT_FOUND));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new OurException(USER_NOT_FOUND));
    }

    private void validateBookingDates(Booking booking) {
        if (booking.getCheckOutDate().isBefore(booking.getCheckInDate())) {
            throw new OurException("Check-out date must come after check-in date");
        }
    }

    private boolean isRoomAvailable(Booking booking, Room room) {
        return room.getBookings().stream().noneMatch(existing -> overlaps(booking, existing));
    }

    private boolean overlaps(Booking a, Booking b) {
        return !a.getCheckOutDate().isBefore(b.getCheckInDate()) &&
                !a.getCheckInDate().isAfter(b.getCheckOutDate());
    }

    private void assignBookingDetails(Booking booking, Room room, User user) {
        booking.setRoom(room);
        booking.setUser(user);
        booking.setBookingConfirmationCode(Utils.generateRandomConfirmationCode(10));
    }
}
