package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.adminDTO.CreateOperatorRequestDTO;
import cdpoitmo.main_service.dto.adminDTO.DiscountRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.dto.bookingDTO.RevenueDTO;
import cdpoitmo.main_service.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Панель администратора", description = "Управление сотрудниками, отчетностью и финансовыми параметрами бронирований.")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
public class AdminController {

    private final AdminService adminService;

    //например запрос такой /api/admin/bookings?start=2024-01-01T00:00:00&end=2024-01-31T23:59:59
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime end
            ) {
        return ResponseEntity.ok(adminService.getBookingsInPeriod(start, end));
    }

    @Operation(
            summary = "Отчет по выручке",
            description = "Формирует отчет о доходах за выбранный период (по дате завершения услуг). Группирует данные по дням."
    )
    @GetMapping("/revenue")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<RevenueDTO>> getRevenueInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to
    ) {
        return ResponseEntity.ok(adminService.getRevenueInPeriod(from, to));
    }

    @Operation(
            summary = "Регистрация оператора",
            description = "Создает нового пользователя с правами OPERATOR. Оператор может управлять каталогом услуг, но не видит отчетность по выручке."
    )
    @PostMapping("/operators")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> createOperator(
            @Valid @RequestBody CreateOperatorRequestDTO operatorRequestDTO
            ) {
        return ResponseEntity.ok(adminService.createOperator(operatorRequestDTO));
    }

    @PatchMapping("/bookings/{id}")
    public ResponseEntity<BookingResponseDTO> timeUpdateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingTimeDTO request
            ) {
        return ResponseEntity.ok(adminService.timeUpdateBooking(id, request));
    }

    @Operation(
            summary = "Назначить персональную скидку",
            description = "Позволяет администратору установить скидку (от 0 до 100) на конкретное бронирование. Цена будет пересчитана автоматически."
    )
    @PatchMapping("/bookings/{id}/discount")
    public ResponseEntity<BookingResponseDTO> setDiscount(
            @PathVariable Long id,
            @Valid @RequestBody DiscountRequestDTO request
    ) {
        return ResponseEntity.ok(adminService.setDiscountBooking(id, request));
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<BookingResponseDTO> cancelBookingByAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.cancelBookingByAdmin(id));
    }
}
