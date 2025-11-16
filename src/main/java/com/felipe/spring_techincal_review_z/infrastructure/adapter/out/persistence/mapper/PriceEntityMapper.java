package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.mapper;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for translating between domain models and persistence entities.
 *
 */
@Component
public class PriceEntityMapper {

    public Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Price.builder()
                .id(entity.getId())
                .brandId(entity.getBrandId())
                .productId(entity.getProductId())
                .priceList(entity.getPriceList())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .priority(entity.getPriority())
                .build();
    }

    public PriceEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }

        PriceEntity entity = new PriceEntity();
        entity.setId(domain.id());
        entity.setBrandId(domain.brandId());
        entity.setProductId(domain.productId());
        entity.setPriceList(domain.priceList());
        entity.setStartDate(domain.startDate());
        entity.setEndDate(domain.endDate());
        entity.setPrice(domain.price());
        entity.setCurrency(domain.currency());
        entity.setPriority(domain.priority());
        return entity;
    }
}