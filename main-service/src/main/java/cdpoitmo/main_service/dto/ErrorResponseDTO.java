package cdpoitmo.main_service.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO (
        LocalDateTime timeStamp,
        int numberStatus,
        String error,
        String message
){
}
