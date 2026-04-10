package com.Jakima_Estate.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceRequestDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    private String propertyAddress;

    private BigDecimal estimatedValue;

    private String message;
}
