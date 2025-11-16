package com.felipe.spring_techincal_review_z.domain.port.out;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public interface PriceRepository {
    Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}