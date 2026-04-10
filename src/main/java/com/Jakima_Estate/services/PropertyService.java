package com.Jakima_Estate.services;

import com.Jakima_Estate.dtos.PropertyDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.enums.PropertyType;
import com.Jakima_Estate.enums.TransactionType;
import com.Jakima_Estate.mappers.PropertyMapper;
import com.Jakima_Estate.models.Property;
import com.Jakima_Estate.models.PropertyImage;
import com.Jakima_Estate.repos.PropertyRepository;
import com.Jakima_Estate.repos.PropertyImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final PropertyMapper propertyMapper;
    private final FileUploadService fileUploadService;

    @Transactional
    public ResponseDTO createPropertyWithImages(PropertyDTO propertyDTO, MultipartFile featuredImage,
                                                List<MultipartFile> additionalImages, MultipartFile propertyVideo) {
        Property property = propertyMapper.toEntity(propertyDTO);

        if (featuredImage != null && !featuredImage.isEmpty()) {
            String featuredImageUrl = fileUploadService.uploadFile(featuredImage);
            property.setFeaturedImage(featuredImageUrl);
        }

        if (propertyVideo != null && !propertyVideo.isEmpty()) {
            String videoUrl = fileUploadService.uploadVideo(propertyVideo);
            property.setPropertyVideoUrl(videoUrl);
        }

        Property savedProperty = propertyRepository.save(property);

        if (additionalImages != null && !additionalImages.isEmpty()) {
            for (MultipartFile image : additionalImages) {
                String imageUrl = fileUploadService.uploadFile(image);
                PropertyImage propertyImage = new PropertyImage();
                propertyImage.setImageUrl(imageUrl);
                propertyImage.setProperty(savedProperty);
                propertyImageRepository.save(propertyImage);
            }
        }

        savedProperty.generateWhatsappLink();
        propertyRepository.save(savedProperty);

        PropertyDTO responseDTO = propertyMapper.toDTO(savedProperty);
        List<String> allImageUrls = propertyImageRepository.findByPropertyId(savedProperty.getId())
                .stream()
                .map(PropertyImage::getImageUrl)
                .collect(Collectors.toList());
        responseDTO.setImageUrls(allImageUrls);
        responseDTO.setFeaturedImage(savedProperty.getFeaturedImage());
        responseDTO.setPropertyVideoUrl(savedProperty.getPropertyVideoUrl());

        return ResponseDTO.success("Property created successfully with images", responseDTO);
    }

    public ResponseDTO createProperty(PropertyDTO propertyDTO) {
        Property property = propertyMapper.toEntity(propertyDTO);
        property.generateWhatsappLink();
        Property savedProperty = propertyRepository.save(property);
        return ResponseDTO.success("Property created successfully", propertyMapper.toDTO(savedProperty));
    }

    public ResponseDTO getAllProperties() {
        List<PropertyDTO> properties = propertyRepository.findAll()
                .stream()
                .map(property -> {
                    PropertyDTO dto = propertyMapper.toDTO(property);
                    List<String> imageUrls = propertyImageRepository.findByPropertyId(property.getId())
                            .stream()
                            .map(PropertyImage::getImageUrl)
                            .collect(Collectors.toList());
                    dto.setImageUrls(imageUrls);
                    dto.setFeaturedImage(property.getFeaturedImage());
                    dto.setWhatsappLink(property.getWhatsappLink());
                    dto.setPropertyVideoUrl(property.getPropertyVideoUrl());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseDTO.success("Properties retrieved successfully", properties);
    }

    public ResponseDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id).orElse(null);
        if (property == null) {
            return ResponseDTO.error("Property not found");
        }
        property.setViewCount(property.getViewCount() + 1);
        propertyRepository.save(property);

        PropertyDTO dto = propertyMapper.toDTO(property);
        List<String> imageUrls = propertyImageRepository.findByPropertyId(property.getId())
                .stream()
                .map(PropertyImage::getImageUrl)
                .collect(Collectors.toList());
        dto.setImageUrls(imageUrls);
        dto.setFeaturedImage(property.getFeaturedImage());
        dto.setWhatsappLink(property.getWhatsappLink());
        dto.setPropertyVideoUrl(property.getPropertyVideoUrl());

        return ResponseDTO.success("Property found", dto);
    }

    @Transactional
    public ResponseDTO addImagesToProperty(Long propertyId, List<MultipartFile> images) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseDTO.error("Property not found");
        }

        List<String> uploadedUrls = new java.util.ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = fileUploadService.uploadFile(image);
            PropertyImage propertyImage = new PropertyImage();
            propertyImage.setImageUrl(imageUrl);
            propertyImage.setProperty(property);
            propertyImageRepository.save(propertyImage);
            uploadedUrls.add(imageUrl);
        }

        return ResponseDTO.success("Images added successfully", uploadedUrls);
    }

    @Transactional
    public ResponseDTO uploadPropertyVideo(Long propertyId, MultipartFile video) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseDTO.error("Property not found");
        }
        String videoUrl = fileUploadService.uploadVideo(video);
        property.setPropertyVideoUrl(videoUrl);
        propertyRepository.save(property);
        return ResponseDTO.success("Video uploaded successfully", videoUrl);
    }

    @Transactional
    public ResponseDTO deleteImage(Long imageId) {
        PropertyImage image = propertyImageRepository.findById(imageId).orElse(null);
        if (image == null) {
            return ResponseDTO.error("Image not found");
        }
        propertyImageRepository.delete(image);
        return ResponseDTO.success("Image deleted successfully", null);
    }

    public ResponseDTO updateProperty(Long id, PropertyDTO propertyDTO) {
        Property existingProperty = propertyRepository.findById(id).orElse(null);
        if (existingProperty == null) {
            return ResponseDTO.error("Property not found");
        }
        propertyMapper.updateEntityFromDTO(propertyDTO, existingProperty);
        existingProperty.generateWhatsappLink();
        Property updatedProperty = propertyRepository.save(existingProperty);
        return ResponseDTO.success("Property updated successfully", propertyMapper.toDTO(updatedProperty));
    }

    public ResponseDTO deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            return ResponseDTO.error("Property not found");
        }
        propertyRepository.deleteById(id);
        return ResponseDTO.success("Property deleted successfully", null);
    }

    public ResponseDTO searchProperties(String location, String propertyType, String transactionType,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        PropertyType propertyTypeEnum = null;
        TransactionType transactionTypeEnum = null;

        if (propertyType != null && !propertyType.isBlank()) {
            try {
                propertyTypeEnum = PropertyType.valueOf(propertyType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseDTO.error("Invalid propertyType. Valid values: APARTMENT, VILLA, TOWNHOUSE, BUNGALOW, MAISONETTE, LAND, COMMERCIAL, OFFICE");
            }
        }

        if (transactionType != null && !transactionType.isBlank()) {
            try {
                transactionTypeEnum = TransactionType.valueOf(transactionType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseDTO.error("Invalid transactionType. Valid values: SALE, RENT, LEASE, AIRBNB");
            }
        }

        List<PropertyDTO> properties = propertyRepository.searchProperties(location, propertyTypeEnum, transactionTypeEnum, minPrice, maxPrice)
                .stream()
                .map(property -> {
                    PropertyDTO dto = propertyMapper.toDTO(property);
                    dto.setWhatsappLink(property.getWhatsappLink());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseDTO.success("Search results", properties);
    }

    public ResponseDTO getFeaturedProperties() {
        List<PropertyDTO> properties = propertyRepository.findByIsFeaturedTrue()
                .stream()
                .map(property -> {
                    PropertyDTO dto = propertyMapper.toDTO(property);
                    dto.setWhatsappLink(property.getWhatsappLink());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseDTO.success("Featured properties", properties);
    }

    public ResponseDTO getPropertiesByType(String transactionType) {
        try {
            TransactionType typeEnum = TransactionType.valueOf(transactionType.toUpperCase());
            List<PropertyDTO> properties = propertyRepository.findByTransactionType(typeEnum)
                    .stream()
                    .map(property -> {
                        PropertyDTO dto = propertyMapper.toDTO(property);
                        dto.setWhatsappLink(property.getWhatsappLink());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseDTO.success("Properties by type", properties);
        } catch (IllegalArgumentException e) {
            return ResponseDTO.error("Invalid transactionType. Valid values: SALE, RENT, LEASE, AIRBNB");
        }
    }

    public String generateWhatsAppLink(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return null;
        }
        property.generateWhatsappLink();
        propertyRepository.save(property);
        return property.getWhatsappLink();
    }
}