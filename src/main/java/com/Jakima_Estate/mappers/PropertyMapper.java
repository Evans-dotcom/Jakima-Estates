package com.Jakima_Estate.mappers;

import com.Jakima_Estate.dtos.PropertyDTO;
import com.Jakima_Estate.models.Property;
import com.Jakima_Estate.models.PropertyImage;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PropertyMapper {

    // ENTITY → DTO
    @Mapping(target = "imageUrls", expression = "java(mapImages(property.getImages()))")
    @Mapping(target = "videoUrls", expression = "java(mapVideos(property))")
    PropertyDTO toDTO(Property property);

    // DTO → ENTITY (ignore system-managed fields)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "enquiryCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "enquiries", ignore = true)
    Property toEntity(PropertyDTO propertyDTO);

    // UPDATE ENTITY FROM DTO
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "enquiryCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "enquiries", ignore = true)
    void updateEntityFromDTO(PropertyDTO dto, @MappingTarget Property entity);

    // 🔹 Custom Mappers
    default List<String> mapImages(List<PropertyImage> images) {
        if (images == null) return List.of();
        return images.stream()
                .map(PropertyImage::getImageUrl)
                .collect(Collectors.toList());
    }

    default List<String> mapVideos(Property property) {
        if (property.getPropertyVideoUrl() == null) return List.of();
        return List.of(property.getPropertyVideoUrl());
    }
}