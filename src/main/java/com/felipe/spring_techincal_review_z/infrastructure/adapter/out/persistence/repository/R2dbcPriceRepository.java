package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.repository;

import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;

public interface R2dbcPriceRepository extends R2dbcRepository<PriceEntity, Long> {

    @Query("SELECT * FROM PRICES WHERE " +
            "PRODUCT_ID = :productId AND " +
            "BRAND_ID = :brandId AND " +
            ":applicationDate BETWEEN START_DATE AND END_DATE")
    Flux<PriceEntity> findApplicablePrices(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    );
}