package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.techServiceDTO.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.techServiceDTO.TechServiceResponseDTO;
import cdpoitmo.main_service.service.TechServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Каталог услуг", description = "Управление списком технических услуг, их описаниями и стоимостью.")
@RestController
@RequestMapping("/api/v1/tech-services")
@RequiredArgsConstructor
@Validated
public class TechServiceController {

    private final TechServiceService techService;

    @GetMapping("/{id}")
    public ResponseEntity<TechServiceResponseDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(techService.getServiceById(id));
    }

    @Operation(summary = "Просмотр всех услуг", description = "Возвращает актуальный прайс-лист со всеми доступными услугами.")
    @GetMapping
    public ResponseEntity<List<TechServiceResponseDTO>> getAllServices() {
        return ResponseEntity.ok(techService.getAllServices());
    }

    @Operation(
            summary = "Добавить новую услугу",
            description = "Добавляет услугу в каталог. Доступно пользователям с ролями ADMIN или OPERATOR."
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<TechServiceResponseDTO> createService(@RequestBody TechServiceRequestDTO techServiceRequestDTO) {
        return ResponseEntity.ok(techService.createService(techServiceRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<TechServiceResponseDTO> updateService(@PathVariable Long id,
                                                                @RequestBody TechServiceRequestDTO techServiceRequestDTO) {
        return ResponseEntity.ok(techService.updateService(id, techServiceRequestDTO));
    }
}
