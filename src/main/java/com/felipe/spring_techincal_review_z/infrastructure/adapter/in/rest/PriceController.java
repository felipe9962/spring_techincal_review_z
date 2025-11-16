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

/**
 * REST controller that handles price queries.
 * Implements the API contract defined in the OpenAPI specification.
 */
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

        log.debug("Received price query - Date: {}, ProductId: {}, BrandId: {}", 
                applicationDate, productId, brandId);

        // Validate input parameters
        if (applicationDate == null || productId == null || brandId == null) {
            log.warn("Invalid request - null parameters detected");
            return Mono.error(new IllegalArgumentException("All parameters are required"));
        }

        if (productId <= 0 || brandId <= 0) {
            log.warn("Invalid request - ProductId: {}, BrandId: {} must be positive", productId, brandId);
            return Mono.error(new IllegalArgumentException("Product ID and Brand ID must be positive"));
        }

        // Convert OffsetDateTime to LocalDateTime
        LocalDateTime localDateTime = applicationDate.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        return getApplicablePriceUseCase
                .getApplicablePrice(localDateTime, productId, brandId)
                .map(price -> {
                    // Map domain Price to PriceResponse
                    PriceResponse response = new PriceResponse();
                    response.setProductId(price.productId());
                    response.setBrandId(price.brandId());
                    response.setPriceList(price.priceList());
                    response.setStartDate(price.startDate().atOffset(ZoneOffset.UTC));
                    response.setEndDate(price.endDate().atOffset(ZoneOffset.UTC));
                    response.setPrice(price.price().doubleValue());
                    response.setCurrency(price.currency());
                    return response;
                });
    }
}