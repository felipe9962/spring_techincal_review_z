package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.mapper;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PriceEntityMapper Unit Tests")
class PriceEntityMapperTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceEntityMapper();
    }

    @Test
    @DisplayName("Should map PriceEntity to Price domain model correctly")
    void shouldMapEntityToDomain() {
        // Given
        PriceEntity entity = new PriceEntity();
        entity.setId(1L);
        entity.setBrandId(1L);
        entity.setProductId(35455L);
        entity.setPriceList(1);
        entity.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        entity.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        entity.setPrice(new BigDecimal("35.50"));
        entity.setCurrency("EUR");
        entity.setPriority(0);

        // When
        Price domain = mapper.toDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.id()).isEqualTo(1L);
        assertThat(domain.brandId()).isEqualTo(1L);
        assertThat(domain.productId()).isEqualTo(35455L);
        assertThat(domain.priceList()).isEqualTo(1);
        assertThat(domain.startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
        assertThat(domain.endDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        assertThat(domain.price()).isEqualByComparingTo(new BigDecimal("35.50"));
        assertThat(domain.currency()).isEqualTo("EUR");
        assertThat(domain.priority()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should map Price domain model to PriceEntity correctly")
    void shouldMapDomainToEntity() {
        // Given
        Price domain = new Price(
                2L, 1L, 35455L, 2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                new BigDecimal("25.45"), "EUR", 1
        );

        // When
        PriceEntity entity = mapper.toEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getBrandId()).isEqualTo(1L);
        assertThat(entity.getProductId()).isEqualTo(35455L);
        assertThat(entity.getPriceList()).isEqualTo(2);
        assertThat(entity.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0));
        assertThat(entity.getEndDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30));
        assertThat(entity.getPrice()).isEqualByComparingTo(new BigDecimal("25.45"));
        assertThat(entity.getCurrency()).isEqualTo("EUR");
        assertThat(entity.getPriority()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return null when mapping null entity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        // When
        Price domain = mapper.toDomain(null);

        // Then
        assertThat(domain).isNull();
    }

    @Test
    @DisplayName("Should return null when mapping null domain to entity")
    void shouldReturnNullWhenDomainIsNull() {
        // When
        PriceEntity entity = mapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    @DisplayName("Should handle BigDecimal precision correctly")
    void shouldHandleBigDecimalPrecision() {
        // Given
        PriceEntity entity = new PriceEntity();
        entity.setId(3L);
        entity.setBrandId(1L);
        entity.setProductId(35455L);
        entity.setPriceList(3);
        entity.setStartDate(LocalDateTime.of(2020, 6, 15, 0, 0));
        entity.setEndDate(LocalDateTime.of(2020, 6, 15, 11, 0));
        entity.setPrice(new BigDecimal("30.50"));
        entity.setCurrency("EUR");
        entity.setPriority(1);

        // When
        Price domain = mapper.toDomain(entity);

        // Then
        assertThat(domain.price()).isEqualByComparingTo(new BigDecimal("30.50"));
        assertThat(domain.price()).isEqualByComparingTo(new BigDecimal("30.500")); // Same value, different scale
    }

    @Test
    @DisplayName("Should preserve all fields during bidirectional mapping")
    void shouldPreserveFieldsDuringBidirectionalMapping() {
        // Given
        PriceEntity originalEntity = new PriceEntity();
        originalEntity.setId(4L);
        originalEntity.setBrandId(1L);
        originalEntity.setProductId(35455L);
        originalEntity.setPriceList(4);
        originalEntity.setStartDate(LocalDateTime.of(2020, 6, 15, 16, 0));
        originalEntity.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        originalEntity.setPrice(new BigDecimal("38.95"));
        originalEntity.setCurrency("EUR");
        originalEntity.setPriority(1);

        // When - Entity -> Domain -> Entity
        Price domain = mapper.toDomain(originalEntity);
        PriceEntity mappedEntity = mapper.toEntity(domain);

        // Then
        assertThat(mappedEntity.getId()).isEqualTo(originalEntity.getId());
        assertThat(mappedEntity.getBrandId()).isEqualTo(originalEntity.getBrandId());
        assertThat(mappedEntity.getProductId()).isEqualTo(originalEntity.getProductId());
        assertThat(mappedEntity.getPriceList()).isEqualTo(originalEntity.getPriceList());
        assertThat(mappedEntity.getStartDate()).isEqualTo(originalEntity.getStartDate());
        assertThat(mappedEntity.getEndDate()).isEqualTo(originalEntity.getEndDate());
        assertThat(mappedEntity.getPrice()).isEqualByComparingTo(originalEntity.getPrice());
        assertThat(mappedEntity.getCurrency()).isEqualTo(originalEntity.getCurrency());
        assertThat(mappedEntity.getPriority()).isEqualTo(originalEntity.getPriority());
    }

    @Test
    @DisplayName("Should handle different currencies correctly")
    void shouldHandleDifferentCurrencies() {
        // Given
        PriceEntity entityUSD = new PriceEntity();
        entityUSD.setId(5L);
        entityUSD.setBrandId(2L);
        entityUSD.setProductId(12345L);
        entityUSD.setPriceList(1);
        entityUSD.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        entityUSD.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        entityUSD.setPrice(new BigDecimal("45.99"));
        entityUSD.setCurrency("USD");
        entityUSD.setPriority(0);

        // When
        Price domain = mapper.toDomain(entityUSD);

        // Then
        assertThat(domain.currency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should handle different priority levels")
    void shouldHandleDifferentPriorityLevels() {
        // Given
        PriceEntity highPriorityEntity = new PriceEntity();
        highPriorityEntity.setId(6L);
        highPriorityEntity.setBrandId(1L);
        highPriorityEntity.setProductId(35455L);
        highPriorityEntity.setPriceList(5);
        highPriorityEntity.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0));
        highPriorityEntity.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30));
        highPriorityEntity.setPrice(new BigDecimal("20.00"));
        highPriorityEntity.setCurrency("EUR");
        highPriorityEntity.setPriority(10);

        // When
        Price domain = mapper.toDomain(highPriorityEntity);

        // Then
        assertThat(domain.priority()).isEqualTo(10);
    }
}


