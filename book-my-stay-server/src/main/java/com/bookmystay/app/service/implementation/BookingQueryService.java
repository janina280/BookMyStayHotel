package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.BookingRepository;
import com.bookmystay.app.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingQueryService {

    private static final String BOOKING_NOT_FOUND = "Booking not found";
    private static final String SUCCESS_MESSAGE = "successful";
    private static final String ERROR_CONTEXT = "Error querying booking: ";

    private final BookingRepository bookingRepository;

    public BookingQueryService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @FunctionalInterface
    private interface BookingQueryCall {
        void execute(Response response) throws OurException;
    }

    private Response handle(BookingQueryCall operation) {
        Response response = new Response();
        try {
            operation.execute(response);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(ERROR_CONTEXT + e.getMessage());
        }
        return response;
    }

    public Response getBookingById(String bookingId) {
        return handle(response -> {
            Booking booking = bookingRepository.findById(Long.valueOf(bookingId))
                    .orElseThrow(() -> new OurException(BOOKING_NOT_FOUND));
            response.setBooking(Utils.mapBookingEntityToBookingDTO(booking));
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        });
    }

    public Response findBookingByConfirmationCode(String confirmationCode) {
        return handle(response -> {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException(BOOKING_NOT_FOUND));
            response.setBooking(Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true));
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        });
    }

    public Response getAllBookings() {
        return handle(response -> {
            List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            response.setBookingList(Utils.mapBookingListEntityToBookingDTO(bookings));
            response.setStatusCode(200);
            response.setMessage(SUCCESS_MESSAGE);
        });
    }
}
