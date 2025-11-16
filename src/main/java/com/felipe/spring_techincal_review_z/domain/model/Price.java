package com.felipe.spring_techincal_review_z.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain model representing a price with its applicable period and priority.
 *
 * <p>Key Concepts:
 * <ul>
 *   <li><b>priceList</b>: Identifier for the price rate/tariff being applied</li>
 *   <li><b>priority</b>: Conflict resolution mechanism - when multiple prices overlap in date ranges,
 *       the one with the highest priority value takes precedence</li>
 *   <li><b>startDate/endDate</b>: Inclusive date range defining when this price is applicable</li>
 * </ul>
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