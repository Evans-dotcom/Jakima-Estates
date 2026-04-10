package com.Jakima_Estate.repos;

import com.Jakima_Estate.models.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByStatus(String status);
    List<ServiceRequest> findByServiceType(String serviceType);
}
