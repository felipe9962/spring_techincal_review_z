package com.felipe.spring_techincal_review_z.domain.service;

import com.felipe.spring_techincal_review_z.application.exception.PriceNotFoundException;
import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PricingService Unit Tests")
class PricingServiceTest {

    @Mock
    private PriceRepository priceRepository;

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService(priceRepository);
    }

    @Test
    @DisplayName("Should return price when found")
    void shouldReturnPriceWhenFound() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price expectedPrice = Price.builder()
                .id(1L)
                .brandId(brandId)
                .productId(productId)
                .priceList(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .priority(0)
                .build();

        when(priceRepository.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Mono.just(expectedPrice));

        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(applicationDate, productId, brandId))
                .assertNext(price -> {
                    assertThat(price).isNotNull();
                    assertThat(price.productId()).isEqualTo(productId);
                    assertThat(price.brandId()).isEqualTo(brandId);
                    assertThat(price.priceList()).isEqualTo(1);
                    assertThat(price.price()).isEqualByComparingTo(new BigDecimal("35.50"));
                    assertThat(price.currency()).isEqualTo("EUR");
                })
                .verifyComplete();

        verify(priceRepository).findApplicablePrice(applicationDate, productId, brandId);
    }

    @Test
    @DisplayName("Should throw PriceNotFoundException when no price found")
    void shouldThrowExceptionWhenNoPriceFound() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2021, 1, 1, 10, 0);
        Long productId = 99999L;
        Long brandId = 1L;

        when(priceRepository.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(applicationDate, productId, brandId))
                .expectErrorMatches(throwable ->
                        throwable instanceof PriceNotFoundException &&
                        throwable.getMessage().equals("No applicable price found for the given parameters")
                )
                .verify();

        verify(priceRepository).findApplicablePrice(applicationDate, productId, brandId);
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void shouldHandleDifferentProductIds() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 16, 0);
        Long productId = 12345L;
        Long brandId = 1L;

        Price expectedPrice = Price.builder()
                .id(2L)
                .brandId(brandId)
                .productId(productId)
                .priceList(4)
                .startDate(LocalDateTime.of(2020, 6, 15, 16, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .price(new BigDecimal("99.99"))
                .currency("EUR")
                .priority(1)
                .build();

        when(priceRepository.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Mono.just(expectedPrice));

        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(applicationDate, productId, brandId))
                .assertNext(price -> {
                    assertThat(price.productId()).isEqualTo(productId);
                    assertThat(price.price()).isEqualByComparingTo(new BigDecimal("99.99"));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle different brand IDs correctly")
    void shouldHandleDifferentBrandIds() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 2L;

        Price expectedPrice = Price.builder()
                .id(3L)
                .brandId(brandId)
                .productId(productId)
                .priceList(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .price(new BigDecimal("40.00"))
                .currency("EUR")
                .priority(0)
                .build();

        when(priceRepository.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Mono.just(expectedPrice));

        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(applicationDate, productId, brandId))
                .assertNext(price -> {
                    assertThat(price.brandId()).isEqualTo(brandId);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should propagate repository errors")
    void shouldPropagateRepositoryErrors() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        RuntimeException expectedException = new RuntimeException("Database connection error");

        when(priceRepository.findApplicablePrice(applicationDate, productId, brandId))
                .thenReturn(Mono.error(expectedException));

        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(applicationDate, productId, brandId))
                .expectError(RuntimeException.class)
                .verify();
    }
}

