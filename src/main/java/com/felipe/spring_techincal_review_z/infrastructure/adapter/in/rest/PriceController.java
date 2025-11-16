package com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest;

import com.felipe.api.DefaultApi;
import com.felipe.api.model.PriceResponse;
import com.felipe.spring_techincal_review_z.domain.port.in.GetApplicablePriceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PriceController implements DefaultApi {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    @Override
    public Mono<PriceResponse> getApplicablePrice(
            OffsetDateTime applicationDate,
            Long productId,
            Long brandId,
            ServerWebExchange exchange) {

        log.info("Incoming price request - endpoint: GET /api/v1/prices, productId: {}, brandId: {}, date: {}", 
                productId, brandId, applicationDate);

        // Validate input parameters
        if (applicationDate == null || productId == null || brandId == null) {
            log.warn("Validation failed - Null parameter(s) detected: applicationDate={}, productId={}, brandId={}", 
                    applicationDate, productId, brandId);
            return Mono.error(new IllegalArgumentException("All parameters are required"));
        }

        if (productId <= 0 || brandId <= 0) {
            log.warn("Validation failed - Invalid parameter values: productId={}, brandId={} (must be positive)", 
                    productId, brandId);
            return Mono.error(new IllegalArgumentException("Product ID and Brand ID must be positive"));
        }

        // Convert OffsetDateTime to LocalDateTime (normalized to UTC)
        LocalDateTime localDateTime = applicationDate.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        log.debug("Converted request date to UTC LocalDateTime: {}", localDateTime);

        return getApplicablePriceUseCase
                .getApplicablePrice(localDateTime, productId, brandId)
                .map(price -> {
                    log.debug("Mapping domain price to response DTO - priceList: {}, price: {}", 
                            price.priceList(), price.price());
                    
                    PriceResponse response = new PriceResponse();
                    response.setProductId(price.productId());
                    response.setBrandId(price.brandId());
                    response.setPriceList(price.priceList());
                    response.setStartDate(price.startDate().atOffset(ZoneOffset.UTC));
                    response.setEndDate(price.endDate().atOffset(ZoneOffset.UTC));
                    response.setPrice(price.price().doubleValue());
                    response.setCurrency(price.currency());
                    
                    log.info("Price request fulfilled successfully - productId: {}, brandId: {}, priceList: {}, price: {} {}", 
                            productId, brandId, price.priceList(), price.price(), price.currency());
                    
                    return response;
                })
                .doOnError(error -> log.error("Price request failed - productId: {}, brandId: {}, error: {}", 
                        productId, brandId, error.getMessage()));
    }
}