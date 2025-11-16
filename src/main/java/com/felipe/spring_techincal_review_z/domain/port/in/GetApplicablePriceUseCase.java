package com.felipe.spring_techincal_review_z.domain.port.in;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public interface GetApplicablePriceUseCase {
    Mono<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}