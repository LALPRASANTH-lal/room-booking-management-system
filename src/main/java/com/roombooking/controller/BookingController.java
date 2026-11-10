
package com.roombooking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.roombooking.entity.Booking;
import com.roombooking.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create Booking
    @PostMapping
    public Booking createBooking(
            @RequestParam Long userId,
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startDateTime,
            @RequestParam LocalDateTime endDateTime) {

        return bookingService.createBooking(
                userId,
                roomId,
                startDateTime,
                endDateTime);
    }

    // Get All Bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get Bookings By User
    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(
            @PathVariable Long userId) {

        return bookingService.getBookingsByUser(userId);
    }
    @PutMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }
    
    @GetMapping("/availability")
    public String checkAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startDateTime,
            @RequestParam LocalDateTime endDateTime) {

        boolean available = bookingService.checkAvailability(
                roomId, startDateTime, endDateTime);

        if (available) {
            return "Room is available";
        } else {
            return "Room is not available";
        }
    }
    @PostMapping("/my-booking")
    public Booking createMyBooking(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startDateTime,
            @RequestParam LocalDateTime endDateTime) {

        return bookingService.createBookingForLoggedInUser(
                roomId, startDateTime, endDateTime);
    }
    
 // View bookings of logged-in user
    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings() {
        return bookingService.getMyBookings();
    }

    // Cancel booking belonging to logged-in user
    @PutMapping("/my-bookings/{bookingId}/cancel")
    public Booking cancelMyBooking(@PathVariable Long bookingId) {
        return bookingService.cancelMyBooking(bookingId);
    }
}
