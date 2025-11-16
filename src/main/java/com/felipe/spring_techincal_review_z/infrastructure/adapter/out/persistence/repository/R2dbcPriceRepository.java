package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.repository;

import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public interface R2dbcPriceRepository extends R2dbcRepository<PriceEntity, Long> {

    /**
     * Finds the single most applicable price entity for the given criteria.
     * 
     * <p>Query Strategy:
     * <ul>
     *   <li>Filters by product ID and brand ID (exact match)</li>
     *   <li>Filters by date range (application date BETWEEN start and end dates)</li>
     *   <li>Orders by priority in descending order (highest priority first)</li>
     *   <li>Returns only the first result (LIMIT 1) for optimal performance</li>
     * </ul>
     *
     * @param applicationDate the date to check for price applicability
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return Mono emitting the highest priority matching price entity, or empty if none found
     */
    @Query("SELECT * FROM PRICES WHERE " +
            "PRODUCT_ID = :productId AND " +
            "BRAND_ID = :brandId AND " +
            ":applicationDate BETWEEN START_DATE AND END_DATE " +
            "ORDER BY PRIORITY DESC " +
            "LIMIT 1")
    Mono<PriceEntity> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    );
}