package com.Jakima_Estate.controllers;
import com.Jakima_Estate.dtos.EnquiryDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.services.EnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
@Tag(name = "Enquiry Management", description = "Endpoints for handling enquiries")
@CrossOrigin(origins = "*")
public class EnquiryController {
    private final EnquiryService enquiryService;

    @PostMapping
    @Operation(summary = "Submit a new enquiry")
    public ResponseDTO createEnquiry(@Valid @RequestBody EnquiryDTO enquiryDTO) {
        return enquiryService.createEnquiry(enquiryDTO);
    }

    @GetMapping
    @Operation(summary = "Get all enquiries")
    public ResponseDTO getAllEnquiries() {
        return enquiryService.getAllEnquiries();
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Get enquiries by property")
    public ResponseDTO getEnquiriesByProperty(@PathVariable Long propertyId) {
        return enquiryService.getEnquiriesByProperty(propertyId);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update enquiry status")
    public ResponseDTO updateEnquiryStatus(@PathVariable Long id, @RequestParam String status) {
        return enquiryService.updateEnquiryStatus(id, status);
    }
}