package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.techServiceDTO.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.techServiceDTO.TechServiceResponseDTO;
import cdpoitmo.main_service.service.TechServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TechServiceResponseDTO>> getAllServices() {
        return ResponseEntity.ok(techService.getAllServices());
    }

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
