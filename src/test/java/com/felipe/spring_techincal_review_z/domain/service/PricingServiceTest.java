package com.felipe.spring_techincal_review_z.domain.service;

import com.felipe.spring_techincal_review_z.domain.exception.PriceNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("Should throw exception when repository is null")
    void shouldThrowExceptionWhenRepositoryIsNull() {
        assertThatThrownBy(() -> new PricingService(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PriceRepository cannot be null");
    }

    @Test
    @DisplayName("Should return price when found")
    void shouldReturnPriceWhenFound() {
        // Given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price expectedPrice = new Price(
                1L, brandId, productId, 1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                new BigDecimal("35.50"), "EUR", 0
        );

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
                                throwable.getMessage().contains("No applicable price found")
                )
                .verify();

        verify(priceRepository).findApplicablePrice(applicationDate, productId, brandId);
    }

    @Test
    @DisplayName("Should throw exception when applicationDate is null")
    void shouldThrowExceptionWhenApplicationDateIsNull() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(null, 35455L, 1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Application date cannot be null")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when productId is null")
    void shouldThrowExceptionWhenProductIdIsNull() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), null, 1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Product ID must be positive")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when productId is zero")
    void shouldThrowExceptionWhenProductIdIsZero() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), 0L, 1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Product ID must be positive")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when productId is negative")
    void shouldThrowExceptionWhenProductIdIsNegative() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), -1L, 1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Product ID must be positive")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when brandId is null")
    void shouldThrowExceptionWhenBrandIdIsNull() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), 35455L, null))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Brand ID must be positive")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when brandId is zero")
    void shouldThrowExceptionWhenBrandIdIsZero() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), 35455L, 0L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Brand ID must be positive")
                )
                .verify();
    }

    @Test
    @DisplayName("Should throw exception when brandId is negative")
    void shouldThrowExceptionWhenBrandIdIsNegative() {
        // When & Then
        StepVerifier.create(pricingService.getApplicablePrice(LocalDateTime.now(), 35455L, -1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Brand ID must be positive")
                )
                .verify();
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