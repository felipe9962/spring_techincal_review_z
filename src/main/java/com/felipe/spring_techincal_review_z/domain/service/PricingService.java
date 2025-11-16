package com.felipe.spring_techincal_review_z.domain.service;

import com.felipe.spring_techincal_review_z.application.exception.PriceNotFoundException;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PricingService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    public Mono<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        log.debug("Fetching applicable price - applicationDate: {}, productId: {}, brandId: {}", 
                applicationDate, productId, brandId);
        
        return priceRepository
                .findApplicablePrice(applicationDate, productId, brandId)
                .doOnNext(price -> log.info("Price found - productId: {}, brandId: {}, priceList: {}, price: {}", 
                        productId, brandId, price.priceList(), price.price()))
                .doOnError(error -> log.error("Error fetching price - productId: {}, brandId: {}, error: {}", 
                        productId, brandId, error.getMessage(), error))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No applicable price found - applicationDate: {}, productId: {}, brandId: {}", 
                            applicationDate, productId, brandId);
                    return Mono.error(new PriceNotFoundException(
                            "No applicable price found for the given parameters"));
                }));
    }
}