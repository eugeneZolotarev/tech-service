package cdpoitmo.main_service.service;

import cdpoitmo.main_service.dto.adminDTO.CreateOperatorRequestDTO;
import cdpoitmo.main_service.dto.adminDTO.DiscountRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.dto.bookingDTO.RevenueDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    List<BookingResponseDTO> getBookingsInPeriod(LocalDateTime start, LocalDateTime end);
    List<RevenueDTO> getRevenueInPeriod(LocalDate from, LocalDate to);
    String createOperator(CreateOperatorRequestDTO createOperatorRequestDTO);
    BookingResponseDTO timeUpdateBooking(Long id, BookingTimeDTO request);
    BookingResponseDTO setDiscountBooking(Long id, DiscountRequestDTO request);
    BookingResponseDTO cancelBookingByAdmin(Long bookingId);
}
