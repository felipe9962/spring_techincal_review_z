package com.felipe.spring_techincal_review_z.domain.port.out;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Repository port for price data access.
 * 
 * <p>Implementation Note: The repository implementation should handle priority-based
 * selection at the database level for optimal performance (ORDER BY PRIORITY DESC LIMIT 1).
 */
public interface PriceRepository {
    
    /**
     * Finds the single most applicable price for the given criteria.
     * 
     * <p>The implementation must:
     * <ul>
     *   <li>Filter by product ID, brand ID, and date range</li>
     *   <li>Return only the price with the highest priority if multiple match</li>
     *   <li>Optimize the query to return a single result (avoid fetching all and filtering in memory)</li>
     * </ul>
     *
     * @param applicationDate the date to check price applicability
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return Mono emitting the applicable price, or empty if none found
     */
    Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}