package cdpoitmo.main_service.service;

import cdpoitmo.main_service.dto.bookingDTO.BookingRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO, String username);

    BookingResponseDTO cancelBooking (Long id, String username);

    BookingResponseDTO updateBookingTime(Long id, BookingTimeDTO bookingTimeDTO, String username);

    List<BookingResponseDTO> getAllMyBookings(String username);

    BookingResponseDTO getBookingById(Long id, String username);

    List<BookingResponseDTO> getBookingHistory(String username);
}
