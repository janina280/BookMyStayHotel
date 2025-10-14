package com.bookmystay.BookMyStay.service.interfac;

import com.bookmystay.BookMyStay.dto.Response;
import com.bookmystay.BookMyStay.entity.Booking;

import java.awt.print.Book;
import java.time.LocalDate;

public interface IBookingService {
    Response getBookingById(String bookingId);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response cancelBooking(Long bookingId);

}
