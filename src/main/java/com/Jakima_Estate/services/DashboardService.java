package com.Jakima_Estate.services;

import com.Jakima_Estate.dtos.DashboardStatsDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.repos.EnquiryRepository;
import com.Jakima_Estate.repos.PropertyRepository;
import com.Jakima_Estate.repos.ServiceRequestRepository;
import com.Jakima_Estate.repos.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PropertyRepository propertyRepository;
    private final EnquiryRepository enquiryRepository;
    private final TestimonialRepository testimonialRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    public ResponseDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalProperties(propertyRepository.count());
        stats.setTotalHappyClients(3800);
        stats.setYearsInMarket(10);
        stats.setEstatesCovered(47);
        stats.setTotalEnquiries(enquiryRepository.count());
        stats.setTotalTestimonials(testimonialRepository.count());
        stats.setTotalServiceRequests(serviceRequestRepository.count());
        stats.setFeaturedProperties(propertyRepository.findByIsFeaturedTrue().size());
        stats.setAvailableProperties(propertyRepository.findByIsAvailableTrue().size());
        stats.setTotalClicks(223);
        stats.setTotalImpressions(17600);
        stats.setAverageCtr(1.3);

        return ResponseDTO.success("Dashboard statistics", stats);
    }
}