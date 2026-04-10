package com.Jakima_Estate.controllers;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.dtos.ServiceRequestDTO;
import com.Jakima_Estate.services.ServiceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Service Requests", description = "Endpoints for service requests (valuation, management, etc)")
@CrossOrigin(origins = "*")
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;

    @PostMapping
    @Operation(summary = "Submit a service request")
    public ResponseDTO createServiceRequest(@Valid @RequestBody ServiceRequestDTO serviceRequestDTO) {
        return serviceRequestService.createServiceRequest(serviceRequestDTO);
    }

    @GetMapping
    @Operation(summary = "Get all service requests")
    public ResponseDTO getAllServiceRequests() {
        return serviceRequestService.getAllServiceRequests();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update request status")
    public ResponseDTO updateRequestStatus(@PathVariable Long id, @RequestParam String status) {
        return serviceRequestService.updateRequestStatus(id, status);
    }
}