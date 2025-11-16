package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.mapper.PriceEntityMapper;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.repository.R2dbcPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Adapter implementing the PriceRepository port using R2DBC.
 * Bridges the domain layer with the reactive database infrastructure.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final R2dbcPriceRepository r2dbcRepository;
    private final PriceEntityMapper mapper;

    @Override
    public Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        log.debug("Executing database query - applicationDate: {}, productId: {}, brandId: {}", 
                applicationDate, productId, brandId);
        
        return r2dbcRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .doOnNext(entity -> log.debug("Database query returned entity - id: {}, priority: {}, priceList: {}", 
                        entity.getId(), entity.getPriority(), entity.getPriceList()))
                .map(mapper::toDomain)
                .doOnSuccess(price -> {
                    if (price == null) {
                        log.debug("No price entity found in database for given criteria");
                    }
                });
    }
}