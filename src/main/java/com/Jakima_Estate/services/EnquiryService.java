package com.Jakima_Estate.services;
import com.Jakima_Estate.dtos.EnquiryDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.models.Enquiry;
import com.Jakima_Estate.models.Property;
import com.Jakima_Estate.repos.EnquiryRepository;
import com.Jakima_Estate.repos.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnquiryService {
    private final EnquiryRepository enquiryRepository;
    private final PropertyRepository propertyRepository;
    private final EmailService emailService;

    public ResponseDTO createEnquiry(EnquiryDTO enquiryDTO) {
        Enquiry enquiry = new Enquiry();
        enquiry.setFullName(enquiryDTO.getFullName());
        enquiry.setEmail(enquiryDTO.getEmail());
        enquiry.setPhone(enquiryDTO.getPhone());
        enquiry.setMessage(enquiryDTO.getMessage());
        enquiry.setEnquiryType(enquiryDTO.getEnquiryType());
        enquiry.setIsWhatsappEnabled(enquiryDTO.getIsWhatsappEnabled() != null ? enquiryDTO.getIsWhatsappEnabled() : false);

        if (enquiryDTO.getPropertyId() != null) {
            Property property = propertyRepository.findById(enquiryDTO.getPropertyId()).orElse(null);
            if (property != null) {
                enquiry.setProperty(property);
                property.setEnquiryCount(property.getEnquiryCount() + 1);
                propertyRepository.save(property);
            }
        }

        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        emailService.sendEnquiryConfirmation(enquiry);
        emailService.sendAdminNotification(enquiry);

        return ResponseDTO.success("Enquiry submitted successfully. We'll contact you soon.", savedEnquiry);
    }

    public ResponseDTO getAllEnquiries() {
        List<Enquiry> enquiries = enquiryRepository.findAll();
        return ResponseDTO.success("Enquiries retrieved", enquiries);
    }

    public ResponseDTO getEnquiriesByProperty(Long propertyId) {
        List<Enquiry> enquiries = enquiryRepository.findByPropertyId(propertyId);
        return ResponseDTO.success("Property enquiries", enquiries);
    }

    public ResponseDTO updateEnquiryStatus(Long id, String status) {
        Enquiry enquiry = enquiryRepository.findById(id).orElse(null);
        if (enquiry == null) {
            return ResponseDTO.error("Enquiry not found");
        }
        enquiry.setStatus(status);
        enquiryRepository.save(enquiry);
        return ResponseDTO.success("Enquiry status updated", enquiry);
    }
}
