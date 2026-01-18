package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.bookingDTO.BookingRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@PreAuthorize("hasAnyAuthority('USER')")
@Validated
@Tag(name = "Кабинет пользователя (Бронирования)", description = "Методы для взаимодействия клиента с сервисом: создание записей, изменение времени и просмотр своей истории.")
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Записаться на услугу",
            description = "Создает новое бронирование. Необходимо передать ID существующей услуги и желаемое время. После создания статус записи — 'CREATED'."
    )
    @PostMapping()
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO bookingRequestDTO,
            @AuthenticationPrincipal UserDetails authUser
            ) {
        return ResponseEntity.ok(
                bookingService.createBooking(bookingRequestDTO, authUser.getUsername())
        );
    }

    @Operation(
            summary = "Отменить бронирование",
            description = "Пользователь может самостоятельно отменить свою запись. Статус записи изменится на 'CANCELLED'."
    )
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.cancelBooking(id, authUser.getUsername())
        );
    }

    @Operation(
            summary = "Перенос времени записи",
            description = "Позволяет пользователю изменить время существующего бронирования, если оно еще не выполнено или не отменено."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBookingTime (
            @PathVariable Long id,
            @RequestBody BookingTimeDTO bookingTimeDTO,
            @AuthenticationPrincipal UserDetails authUser) {
            return ResponseEntity.ok(
                    bookingService.updateBookingTime(id, bookingTimeDTO, authUser.getUsername())
            );
    }

    @Operation(
            summary = "Список моих активных записей",
            description = "Возвращает список всех актуальных бронирований текущего пользователя со статусом 'CREATED'."
    )
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllMyBookings (
            @AuthenticationPrincipal UserDetails authUser
    ) {
        return ResponseEntity.ok(
                bookingService.getAllMyBookings(authUser.getUsername())
        );
    }

    @Operation(
            summary = "Получить бронирование по id",
            description = "Получение данных о записи по его id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById (
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.getBookingById(id, authUser.getUsername())
        );
    }

    @Operation(
            summary = "История моих посещений",
            description = "Возвращает список всех завершенных (COMPLETED) или отмененных (CANCELLED) записей пользователя."
    )
    @GetMapping("/history")
    public ResponseEntity<List<BookingResponseDTO>> getBookingHistory (
            @AuthenticationPrincipal UserDetails authUser) {
        return ResponseEntity.ok(
                bookingService.getBookingHistory(authUser.getUsername())
        );
    }
}
