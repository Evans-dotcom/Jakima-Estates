package com.Jakima_Estate.controllers;

import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.services.WhatsAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
@Tag(name = "WhatsApp Integration", description = "Endpoints for WhatsApp chat integration")
@CrossOrigin(origins = "*")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Generate WhatsApp link for property enquiry")
    public ResponseDTO getWhatsAppLink(
            @PathVariable Long propertyId,
            @RequestParam String customerName,
            @RequestParam String customerPhone) {
        String link = whatsAppService.generatePropertyWhatsAppLink(propertyId, customerName, customerPhone);
        if (link == null) {
            return ResponseDTO.error("Property not found");
        }
        return ResponseDTO.success("WhatsApp link generated", link);
    }

    @PostMapping("/quick-enquiry/{propertyId}")
    @Operation(summary = "Send quick WhatsApp enquiry")
    public ResponseDTO quickEnquiry(
            @PathVariable Long propertyId,
            @RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam(required = false) String message) {
        String msg = message != null ? message : "I'm interested in this property";
        return whatsAppService.sendQuickEnquiry(propertyId, customerName, customerPhone, msg);
    }
}