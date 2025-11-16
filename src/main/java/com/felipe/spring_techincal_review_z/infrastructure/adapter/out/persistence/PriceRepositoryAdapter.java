package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.mapper.PriceEntityMapper;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.repository.R2dbcPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

    private final R2dbcPriceRepository r2dbcRepository;
    private final PriceEntityMapper mapper;

    @Override
    public Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return r2dbcRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .map(mapper::toDomain);
    }
}