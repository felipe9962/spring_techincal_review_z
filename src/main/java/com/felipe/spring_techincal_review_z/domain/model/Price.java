package com.felipe.spring_techincal_review_z.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain model representing a price with its applicable period and priority.
 * This follows Domain-Driven Design principles using Java records for immutability.
 */
@Builder(toBuilder = true)
public record Price(
        Long id,
        Long brandId,
        Long productId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency,
        Integer priority
) {
}