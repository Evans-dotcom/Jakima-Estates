package com.Jakima_Estate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalProperties;
    private long totalHappyClients;
    private long yearsInMarket;
    private long estatesCovered;
    private long totalEnquiries;
    private long totalTestimonials;
    private long totalServiceRequests;
    private long featuredProperties;
    private long availableProperties;
    private long totalClicks;
    private double totalImpressions;
    private double averageCtr;
}