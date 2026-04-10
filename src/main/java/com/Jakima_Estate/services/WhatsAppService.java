package com.Jakima_Estate.services;

import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.models.Enquiry;
import com.Jakima_Estate.models.Property;
import com.Jakima_Estate.repos.EnquiryRepository;
import com.Jakima_Estate.repos.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final PropertyRepository propertyRepository;
    private final EnquiryRepository enquiryRepository;

    @Value("${whatsapp.business.phone}")
    private String businessPhone;

    public String generatePropertyWhatsAppLink(Long propertyId, String customerName, String customerPhone) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return null;
        }

        String encodedTitle = URLEncoder.encode(property.getTitle(), StandardCharsets.UTF_8);
        String encodedLocation = URLEncoder.encode(property.getLocation(), StandardCharsets.UTF_8);
        String encodedCustomerName = URLEncoder.encode(customerName, StandardCharsets.UTF_8);
        String encodedCustomerPhone = URLEncoder.encode(customerPhone, StandardCharsets.UTF_8);

        String message = "Hello, I'm interested in property: " +
                "Title: " + property.getTitle() + ". " +
                "Location: " + property.getLocation() + ". " +
                "Price: KES " + property.getPrice() + ". " +
                "My name: " + customerName + ". " +
                "My phone: " + customerPhone;

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "https://wa.me/" + businessPhone + "?text=" + encodedMessage;
    }

    public ResponseDTO sendQuickEnquiry(Long propertyId, String customerName, String customerPhone, String message) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseDTO.error("Property not found");
        }

        Enquiry enquiry = new Enquiry();
        enquiry.setFullName(customerName);
        enquiry.setPhone(customerPhone);
        enquiry.setMessage(message);
        enquiry.setEnquiryType("WHATSAPP_QUICK");
        enquiry.setIsWhatsappEnabled(true);
        enquiry.setProperty(property);
        enquiry.setEmail(customerPhone + "@whatsapp.user");

        enquiryRepository.save(enquiry);

        String whatsappLink = generatePropertyWhatsAppLink(propertyId, customerName, customerPhone);

        return ResponseDTO.success("Click the link to chat on WhatsApp", whatsappLink);
    }
}