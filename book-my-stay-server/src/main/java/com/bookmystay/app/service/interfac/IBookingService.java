package com.bookmystay.app.service.interfac;


import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;

public interface IBookingService {
    Response getBookingById(String bookingId);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response cancelBooking(Long bookingId);

}
