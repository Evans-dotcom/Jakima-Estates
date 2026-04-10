package com.Jakima_Estate.controllers;

import com.Jakima_Estate.dtos.PropertyDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.enums.PropertyType;
import com.Jakima_Estate.enums.TransactionType;
import com.Jakima_Estate.services.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Property management endpoints")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService propertyService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Create property with images and optional video",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)))
    @PostMapping(value = "/with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> createPropertyWithImages(
            @RequestParam("property") String propertyJson,
            @RequestPart(value = "featuredImage", required = false) MultipartFile featuredImage,
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @RequestPart(value = "propertyVideo", required = false) MultipartFile propertyVideo) {
        try {
            PropertyDTO propertyDTO = objectMapper.readValue(propertyJson, PropertyDTO.class);
            return ResponseEntity.ok(propertyService.createPropertyWithImages(
                    propertyDTO, featuredImage, additionalImages, propertyVideo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Invalid property JSON: " + e.getMessage()));
        }
    }

    @Operation(summary = "Create property without images")
    @PostMapping
    public ResponseEntity<ResponseDTO> createProperty(
            @org.springframework.web.bind.annotation.RequestBody PropertyDTO propertyDTO) {
        return ResponseEntity.ok(propertyService.createProperty(propertyDTO));
    }

    @Operation(summary = "Get all properties")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @Operation(summary = "Get property by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @Operation(summary = "Update property")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateProperty(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody PropertyDTO propertyDTO) {
        return ResponseEntity.ok(propertyService.updateProperty(id, propertyDTO));
    }

    @Operation(summary = "Delete property")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteProperty(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.deleteProperty(id));
    }

    @Operation(summary = "Add images to existing property",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)))
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> addImages(
            @PathVariable Long id,
            @RequestPart("images") List<MultipartFile> images) {
        return ResponseEntity.ok(propertyService.addImagesToProperty(id, images));
    }

    @Operation(summary = "Upload video to existing property",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)))
    @PostMapping(value = "/{id}/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> uploadVideo(
            @PathVariable Long id,
            @RequestPart("video") MultipartFile video) {
        return ResponseEntity.ok(propertyService.uploadPropertyVideo(id, video));
    }

    @Operation(summary = "Delete an image by ID")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ResponseDTO> deleteImage(@PathVariable Long imageId) {
        return ResponseEntity.ok(propertyService.deleteImage(imageId));
    }

    @Operation(summary = "Search properties")
    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(propertyService.searchProperties(
                location,
                propertyType != null ? propertyType.name() : null,
                transactionType != null ? transactionType.name() : null,
                minPrice,
                maxPrice));
    }

    @Operation(summary = "Get featured properties")
    @GetMapping("/featured")
    public ResponseEntity<ResponseDTO> getFeaturedProperties() {
        return ResponseEntity.ok(propertyService.getFeaturedProperties());
    }

    @Operation(summary = "Get properties by transaction type")
    @GetMapping("/type/{transactionType}")
    public ResponseEntity<ResponseDTO> getByType(@PathVariable TransactionType transactionType) {
        return ResponseEntity.ok(propertyService.getPropertiesByType(transactionType.name()));
    }

    @Operation(summary = "Generate WhatsApp link for a property")
    @GetMapping("/{id}/whatsapp")
    public ResponseEntity<String> getWhatsAppLink(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.generateWhatsAppLink(id));
    }
}