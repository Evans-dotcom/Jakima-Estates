package com.Jakima_Estate.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestimonialDTO {
    @NotBlank(message = "Client name is required")
    private String clientName;

    @NotBlank(message = "Testimonial content is required")
    private String content;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String clientType;
}