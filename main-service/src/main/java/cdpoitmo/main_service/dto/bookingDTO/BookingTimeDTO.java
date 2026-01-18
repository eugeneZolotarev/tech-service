package cdpoitmo.main_service.dto.bookingDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingTimeDTO (
        @Future(message = "Время бронирования должно быть позже нынешнего")
        @NotNull(message = "Время бронирования не может быть пустым")
        LocalDateTime appointmentTime
) {
}
