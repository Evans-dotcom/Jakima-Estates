package com.Jakima_Estate.controllers;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics and metrics")
@CrossOrigin(origins = "*")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseDTO getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
}