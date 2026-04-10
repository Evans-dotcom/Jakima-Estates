package com.Jakima_Estate.services;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.dtos.ServiceRequestDTO;
import com.Jakima_Estate.models.ServiceRequest;
import com.Jakima_Estate.repos.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final EmailService emailService;

    public ResponseDTO createServiceRequest(ServiceRequestDTO dto) {
        ServiceRequest request = new ServiceRequest();
        request.setFullName(dto.getFullName());
        request.setEmail(dto.getEmail());
        request.setPhone(dto.getPhone());
        request.setServiceType(dto.getServiceType());
        request.setPropertyAddress(dto.getPropertyAddress());
        request.setEstimatedValue(dto.getEstimatedValue());
        request.setMessage(dto.getMessage());

        ServiceRequest saved = serviceRequestRepository.save(request);

        emailService.sendServiceRequestConfirmation(request);
        emailService.sendAdminServiceRequestNotification(request);

        return ResponseDTO.success("Service request submitted successfully", saved);
    }

    public ResponseDTO getAllServiceRequests() {
        List<ServiceRequest> requests = serviceRequestRepository.findAll();
        return ResponseDTO.success("Service requests", requests);
    }

    public ResponseDTO updateRequestStatus(Long id, String status) {
        ServiceRequest request = serviceRequestRepository.findById(id).orElse(null);
        if (request == null) {
            return ResponseDTO.error("Request not found");
        }
        request.setStatus(status);
        serviceRequestRepository.save(request);
        return ResponseDTO.success("Status updated", request);
    }
}