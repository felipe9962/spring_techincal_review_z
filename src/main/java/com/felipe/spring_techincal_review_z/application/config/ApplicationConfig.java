package com.felipe.spring_techincal_review_z.application.config;

import com.felipe.spring_techincal_review_z.domain.port.in.GetApplicablePriceUseCase;
import com.felipe.spring_techincal_review_z.domain.port.out.PriceRepository;
import com.felipe.spring_techincal_review_z.domain.service.PricingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepository priceRepository) {
        return new PricingService(priceRepository);
    }
}
