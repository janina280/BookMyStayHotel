package com.bookmystay.app.controller;

import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.Booking;
import com.bookmystay.app.service.interfac.IBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long roomId,
                                                 @PathVariable Long userId,
                                                 @RequestBody Booking bookingRequest) {
        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{code}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String code) {
        Response response = bookingService.findBookingByConfirmationCode(code);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel-booking/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/booking/update/{id}")
    public ResponseEntity<Response> updateBooking(
            @PathVariable Long id,
            @RequestParam LocalDate newCheckIn,
            @RequestParam LocalDate newCheckOut) {

        Response response = bookingService.updateBooking(id, newCheckIn, newCheckOut);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<Response> getBookingHistory(@PathVariable Long userId) {
        Response response = bookingService.getBookingHistoryByUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
