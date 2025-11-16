package com.felipe.spring_techincal_review_z.domain.service;

import com.felipe.spring_techincal_review_z.application.exception.PriceNotFoundException;
import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.domain.port.in.GetApplicablePriceUseCase;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PricingService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    public Mono<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return priceRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .switchIfEmpty(Mono.error(
                        new PriceNotFoundException("No applicable price found for the given parameters")
                ));
    }
}