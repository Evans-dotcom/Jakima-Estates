package com.Jakima_Estate.repos;

import com.Jakima_Estate.models.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    List<Enquiry> findByPropertyId(Long propertyId);
    List<Enquiry> findByStatus(String status);
    List<Enquiry> findByEmail(String email);
}