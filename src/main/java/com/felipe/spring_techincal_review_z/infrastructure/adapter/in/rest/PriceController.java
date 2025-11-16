package com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest;

import com.felipe.spring_techincal_review_z.domain.port.in.GetApplicablePriceUseCase;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest.api.DefaultApi;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest.model.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequiredArgsConstructor
public class PriceController implements DefaultApi {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    @Override
    public Mono<ResponseEntity<PriceResponse>> getApplicablePrice(
            OffsetDateTime applicationDate,
            Long productId,
            Long brandId,
            ServerWebExchange exchange) {

        // Convert OffsetDateTime to LocalDateTime
        LocalDateTime localDateTime = applicationDate.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        return getApplicablePriceUseCase
                .getApplicablePrice(localDateTime, productId, brandId)
                .map(price -> {
                    // Map domain Price to PriceResponse
                    PriceResponse response = new PriceResponse();
                    response.setProductId(price.getProductId());
                    response.setBrandId(price.getBrandId());
                    response.setPriceList(price.getPriceList());
                    response.setStartDate(price.getStartDate().atOffset(ZoneOffset.UTC));
                    response.setEndDate(price.getEndDate().atOffset(ZoneOffset.UTC));
                    response.setPrice(price.getPrice().doubleValue());
                    response.setCurrency(price.getCurrency());
                    return ResponseEntity.ok(response);
                });
    }
}