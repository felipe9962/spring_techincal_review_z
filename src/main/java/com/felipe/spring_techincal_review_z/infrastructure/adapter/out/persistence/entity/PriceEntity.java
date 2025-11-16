package com.felipe.spring_techincal_review_z.infrastructure.adapter.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("PRICES")
public class PriceEntity {
    @Id
    private Long id;

    @Column("BRAND_ID")
    private Long brandId;

    @Column("START_DATE")
    private LocalDateTime startDate;

    @Column("END_DATE")
    private LocalDateTime endDate;

    @Column("PRICE_LIST")
    private Integer priceList;

    @Column("PRODUCT_ID")
    private Long productId;

    @Column("PRIORITY")
    private Integer priority;

    @Column("PRICE")
    private BigDecimal price;

    @Column("CURR")
    private String currency;
}