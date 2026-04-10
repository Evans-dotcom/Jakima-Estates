package com.Jakima_Estate.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String serviceType;

    private String propertyAddress;

    private BigDecimal estimatedValue;

    private String message;

    private String status;

    private LocalDateTime requestDate;

    @PrePersist
    protected void onCreate() {
        requestDate = LocalDateTime.now();
        status = "PENDING";
    }
}