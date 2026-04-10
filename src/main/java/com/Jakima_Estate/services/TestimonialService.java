package com.Jakima_Estate.services;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.dtos.TestimonialDTO;
import com.Jakima_Estate.models.Testimonial;
import com.Jakima_Estate.repos.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonialService {
    private final TestimonialRepository testimonialRepository;

    public ResponseDTO createTestimonial(TestimonialDTO testimonialDTO) {
        Testimonial testimonial = new Testimonial();
        testimonial.setClientName(testimonialDTO.getClientName());
        testimonial.setContent(testimonialDTO.getContent());
        testimonial.setRating(testimonialDTO.getRating());
        testimonial.setClientType(testimonialDTO.getClientType());
        testimonial.setIsApproved(false);

        Testimonial saved = testimonialRepository.save(testimonial);
        return ResponseDTO.success("Testimonial submitted for approval", saved);
    }

    public ResponseDTO getApprovedTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.findByIsApprovedTrue();
        return ResponseDTO.success("Approved testimonials", testimonials);
    }

    public ResponseDTO getAllTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.findAll();
        return ResponseDTO.success("All testimonials", testimonials);
    }

    public ResponseDTO approveTestimonial(Long id) {
        Testimonial testimonial = testimonialRepository.findById(id).orElse(null);
        if (testimonial == null) {
            return ResponseDTO.error("Testimonial not found");
        }
        testimonial.setIsApproved(true);
        testimonialRepository.save(testimonial);
        return ResponseDTO.success("Testimonial approved", testimonial);
    }

    public ResponseDTO deleteTestimonial(Long id) {
        testimonialRepository.deleteById(id);
        return ResponseDTO.success("Testimonial deleted", null);
    }
}