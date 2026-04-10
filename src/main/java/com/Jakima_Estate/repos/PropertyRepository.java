package com.Jakima_Estate.repos;

import com.Jakima_Estate.enums.PropertyType;
import com.Jakima_Estate.enums.TransactionType;
import com.Jakima_Estate.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByTransactionType(TransactionType transactionType);
    List<Property> findByPropertyType(PropertyType propertyType);
    List<Property> findByLocationContainingIgnoreCase(String location);
    List<Property> findByIsFeaturedTrue();
    List<Property> findByIsAvailableTrue();

    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
            "(:transactionType IS NULL OR p.transactionType = :transactionType) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Property> searchProperties(@Param("location") String location,
                                    @Param("propertyType") PropertyType propertyType,
                                    @Param("transactionType") TransactionType transactionType,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice);
}