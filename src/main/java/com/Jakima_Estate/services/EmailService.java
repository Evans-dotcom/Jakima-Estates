package com.Jakima_Estate.services;

import com.Jakima_Estate.models.Enquiry;
import com.Jakima_Estate.models.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEnquiryConfirmation(Enquiry enquiry) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(enquiry.getEmail());
        message.setSubject("Jakima Properties - We've received your enquiry");
        message.setText("Dear " + enquiry.getFullName() + ",\n\n" +
                "Thank you for contacting Jakima Properties.\n\n" +
                "We have received your enquiry regarding " +
                (enquiry.getProperty() != null ? enquiry.getProperty().getTitle() : "our properties") +
                ".\n\n" +
                "One of our agents will contact you within 24 hours.\n\n" +
                "Enquiry Reference: #" + enquiry.getId() + "\n" +
                "Enquiry Type: " + enquiry.getEnquiryType() + "\n\n" +
                "For quick assistance, you can also WhatsApp us on +254700000000.\n\n" +
                "Best regards,\n" +
                "Jakima Properties Team");
        mailSender.send(message);
    }

    public void sendAdminNotification(Enquiry enquiry) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("admin@jakimaestate.com");
        message.setSubject("New Enquiry Received - Jakima Properties");
        message.setText("New enquiry received:\n\n" +
                "Name: " + enquiry.getFullName() + "\n" +
                "Email: " + enquiry.getEmail() + "\n" +
                "Phone: " + enquiry.getPhone() + "\n" +
                "Type: " + enquiry.getEnquiryType() + "\n" +
                "Property: " + (enquiry.getProperty() != null ? enquiry.getProperty().getTitle() : "Not specified") + "\n" +
                "Message: " + enquiry.getMessage() + "\n\n" +
                "WhatsApp Enabled: " + enquiry.getIsWhatsappEnabled());
        mailSender.send(message);
    }

    public void sendServiceRequestConfirmation(ServiceRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Jakima Properties - Service Request Confirmation");
        message.setText("Dear " + request.getFullName() + ",\n\n" +
                "Thank you for requesting our " + request.getServiceType() + " service.\n\n" +
                "Reference: #SR" + request.getId() + "\n" +
                "Service Type: " + request.getServiceType() + "\n\n" +
                "Our specialist will contact you within 24 hours.\n\n" +
                "Regards,\n" +
                "Jakima Properties");
        mailSender.send(message);
    }

    public void sendAdminServiceRequestNotification(ServiceRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("admin@jakimaestate.com");
        message.setSubject("New Service Request - Jakima Properties");
        message.setText("New service request:\n\n" +
                "Name: " + request.getFullName() + "\n" +
                "Email: " + request.getEmail() + "\n" +
                "Phone: " + request.getPhone() + "\n" +
                "Service: " + request.getServiceType() + "\n" +
                "Property Address: " + request.getPropertyAddress() + "\n" +
                "Estimated Value: " + request.getEstimatedValue() + "\n" +
                "Message: " + request.getMessage());
        mailSender.send(message);
    }
}
