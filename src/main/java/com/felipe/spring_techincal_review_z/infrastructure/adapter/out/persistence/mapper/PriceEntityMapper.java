package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.mapper;

import com.felipe.spring_techincal_review_z.domain.model.Price;
import com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

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
        entity.setId(domain.getId());
        entity.setBrandId(domain.getBrandId());
        entity.setProductId(domain.getProductId());
        entity.setPriceList(domain.getPriceList());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setPrice(domain.getPrice());
        entity.setCurrency(domain.getCurrency());
        entity.setPriority(domain.getPriority());
        return entity;
    }
}