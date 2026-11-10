
package com.roombooking.service;

import java.time.LocalDateTime;
import com.roombooking.exception.BookingConflictException;
import java.util.List;

import org.springframework.stereotype.Service;
import com.roombooking.entity.Booking;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import com.roombooking.repository.BookingRepository;
import com.roombooking.repository.RoomRepository;
import com.roombooking.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Booking cancelBooking(Long bookingId) {

        // Find booking by ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        // Check whether already cancelled
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Booking is already cancelled");
        }

        // Change status
        booking.setStatus("CANCELLED");

        // Save updated booking
        return bookingRepository.save(booking);
    }
    // Create Booking
    public Booking createBooking(
            Long userId,
            Long roomId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        // Validate dates
        if (startDateTime == null || endDateTime == null ||
                !endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("Invalid booking dates");
        }

        // Check User
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Check Room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException("Room not found"));

        // Check existing bookings
        List<Booking> existingBookings =
                bookingRepository.findByRoomId(roomId);

        for (Booking booking : existingBookings) {

            if ("CONFIRMED".equals(booking.getStatus())) {

                boolean overlap =
                        startDateTime.isBefore(booking.getEndDateTime()) &&
                        endDateTime.isAfter(booking.getStartDateTime());

                if (overlap) {
                    throw new BookingConflictException(
                        "Room is already booked for this time"
                    );
                }
            }
        }

        // Create new Booking
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartDateTime(startDateTime);
        booking.setEndDateTime(endDateTime);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    // Get All Bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get Bookings By User
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    
    
    
    public boolean checkAvailability(
            Long roomId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        // Validate date and time
        if (!startDateTime.isBefore(endDateTime)) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        // Check whether room exists
        roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Get all bookings for this room
        List<Booking> bookings = bookingRepository.findByRoomId(roomId);

        // Check overlapping bookings
        for (Booking booking : bookings) {

            if ("CONFIRMED".equals(booking.getStatus())) {

                boolean overlap =
                        startDateTime.isBefore(booking.getEndDateTime())
                        && endDateTime.isAfter(booking.getStartDateTime());

                if (overlap) {
                    return false;
                }
            }
        }

        return true;
    }
    public Booking createBookingForLoggedInUser(
            Long roomId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        // Get logged-in user's email from JWT authentication
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        // Find the logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Reuse your existing booking logic
        return createBooking(
                user.getId(),
                roomId,
                startDateTime,
                endDateTime);
    }
    //
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    //view my booking
    public List<Booking> getMyBookings() {

        User user = getLoggedInUser();

        return bookingRepository.findByUserId(user.getId());
    }
 // for secure cancellation
    public Booking cancelMyBooking(Long bookingId) {

        User user = getLoggedInUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot cancel another user's booking");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking is already cancelled");
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }


    
}
