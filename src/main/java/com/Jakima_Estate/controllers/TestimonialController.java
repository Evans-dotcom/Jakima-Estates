package com.Jakima_Estate.controllers;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.dtos.TestimonialDTO;
import com.Jakima_Estate.services.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testimonials")
@RequiredArgsConstructor
@Tag(name = "Testimonial Management", description = "Endpoints for client testimonials")
@CrossOrigin(origins = "*")
public class TestimonialController {
    private final TestimonialService testimonialService;

    @PostMapping
    @Operation(summary = "Submit a testimonial")
    public ResponseDTO createTestimonial(@Valid @RequestBody TestimonialDTO testimonialDTO) {
        return testimonialService.createTestimonial(testimonialDTO);
    }

    @GetMapping("/approved")
    @Operation(summary = "Get approved testimonials")
    public ResponseDTO getApprovedTestimonials() {
        return testimonialService.getApprovedTestimonials();
    }

    @GetMapping
    @Operation(summary = "Get all testimonials (admin)")
    public ResponseDTO getAllTestimonials() {
        return testimonialService.getAllTestimonials();
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve a testimonial")
    public ResponseDTO approveTestimonial(@PathVariable Long id) {
        return testimonialService.approveTestimonial(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete testimonial")
    public ResponseDTO deleteTestimonial(@PathVariable Long id) {
        return testimonialService.deleteTestimonial(id);
    }
}