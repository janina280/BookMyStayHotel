package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.service.interfac.IBookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {

        private final BookingQueryService queryService;
        private final BookingManagementService managementService;

        public BookingService(BookingQueryService queryService,
                              BookingManagementService managementService) {
            this.queryService = queryService;
            this.managementService = managementService;
        }

        public Response getBookingById(String bookingId) {
            return queryService.getBookingById(bookingId);
        }

        public Response findBookingByConfirmationCode(String code) {
            return queryService.findBookingByConfirmationCode(code);
        }

        public Response getAllBookings() {
            return queryService.getAllBookings();
        }

        public Response saveBooking(Long roomId, Long userId, Booking booking) {
            return managementService.saveBooking(roomId, userId, booking);
        }

        public Response cancelBooking(Long bookingId) {
            return managementService.cancelBooking(bookingId);
        }
    }



