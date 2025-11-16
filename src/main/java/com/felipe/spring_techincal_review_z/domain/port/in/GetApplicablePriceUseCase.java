package com.felipe.spring_techincal_review_z.domain.port.in;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Use case for retrieving the applicable price for a product.
 */
public interface GetApplicablePriceUseCase {
    
    /**
     * Retrieves the most applicable price based on date range and priority.
     *
     * @param applicationDate the date/time when the price should be applicable
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return Mono emitting the applicable price, or error if not found
     */
    Mono<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}