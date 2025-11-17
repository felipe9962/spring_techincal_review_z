package com.felipe.spring_techincal_review_z.domain.model;

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
    public static Price create(Long id, Long brandId, Long productId, Integer priceList,
                               LocalDateTime startDate, LocalDateTime endDate,
                               BigDecimal price, String currency, Integer priority) {
        return new Price(id, brandId, productId, priceList, startDate, endDate, price, currency, priority);
    }

    public Price withPrice(BigDecimal newPrice) {
        return new Price(id, brandId, productId, priceList, startDate, endDate, newPrice, currency, priority);
    }

    public Price withPriority(Integer newPriority) {
        return new Price(id, brandId, productId, priceList, startDate, endDate, price, currency, newPriority);
    }
}