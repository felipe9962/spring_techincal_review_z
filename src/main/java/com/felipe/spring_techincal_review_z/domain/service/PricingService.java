package com.felipe.spring_techincal_review_z.domain.service;

import com.felipe.spring_techincal_review_z.domain.exception.PriceNotFoundException;
import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.domain.port.in.GetApplicablePriceUseCase;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service implementing the pricing business logic.
 * Orchestrates price retrieval with priority-based selection.
 */
public class PricingService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    public PricingService(PriceRepository priceRepository) {
        if (priceRepository == null) {
            throw new IllegalArgumentException("PriceRepository cannot be null");
        }
        this.priceRepository = priceRepository;
    }

    @Override
    public Mono<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        // Input validation at domain level
        validateInputs(applicationDate, productId, brandId);

        return priceRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new PriceNotFoundException(
                                String.format("No applicable price found for productId=%d, brandId=%d, date=%s",
                                        productId, brandId, applicationDate)))
                ));
    }

    private void validateInputs(LocalDateTime applicationDate, Long productId, Long brandId) {
        if (applicationDate == null) {
            throw new IllegalArgumentException("Application date cannot be null");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be positive");
        }
    }
}