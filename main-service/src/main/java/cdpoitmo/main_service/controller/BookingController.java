package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.bookingDTO.BookingRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingResponseDTO> createBooking(
            BookingRequestDTO bookingRequestDTO,
            @AuthenticationPrincipal UserDetails authUser
            ) {
        return ResponseEntity.ok(
                bookingService.createBooking(bookingRequestDTO, authUser.getUsername())
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.cancelBooking(id, authUser.getUsername())
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBookingTime (
            @PathVariable Long id,
            @RequestBody BookingTimeDTO bookingTimeDTO,
            @AuthenticationPrincipal UserDetails authUser) {
            return ResponseEntity.ok(
                    bookingService.updateBookingTime(id, bookingTimeDTO, authUser.getUsername())
            );
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllMyBookings (
            @AuthenticationPrincipal UserDetails authUser
    ) {
        return ResponseEntity.ok(
                bookingService.getAllMyBookings(authUser.getUsername())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById (
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.getBookingById(id, authUser.getUsername())
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<BookingResponseDTO>> getBookingHistory (
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.getBookingHistory(authUser.getUsername())
        );
    }
}
