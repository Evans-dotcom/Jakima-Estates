package com.Jakima_Estate.dtos;

import com.Jakima_Estate.enums.PropertyType;
import com.Jakima_Estate.enums.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyDTO {
    private Long id;
    private String title;
    private String description;
    private PropertyType propertyType;
    private TransactionType transactionType;
    private BigDecimal price;
    private String location;
    private String estate;
    private String area;
    private Double landSize;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer parkingSpaces;
    private Boolean hasGarden;
    private Boolean hasBalcony;
    private Boolean hasFurnished;
    private String featuredImage;
    private String virtualTourUrl;
    private String propertyVideoUrl;
    private Boolean isFeatured;
    private Boolean isAvailable;
    private String whatsappLink;
    private List<String> imageUrls;
    private List<String> videoUrls;
}