package com.Jakima_Estate.models;

import com.Jakima_Estate.enums.PropertyType;
import com.Jakima_Estate.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 15, scale = 2)
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
    private Boolean isFeatured = false;
    private Boolean isAvailable = true;
    private Integer viewCount = 0;
    private Integer enquiryCount = 0;
    private String whatsappLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PropertyImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Enquiry> enquiries = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        generateWhatsappLink();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        generateWhatsappLink();
    }

    public void generateWhatsappLink() {
        String message = "Hello%2C%20I%27m%20interested%20in%20" + this.title + "%20located%20in%20" + this.location + "%20priced%20at%20KES%20" + this.price;
        this.whatsappLink = "https://wa.me/254719127100?text=" + message;
    }
}