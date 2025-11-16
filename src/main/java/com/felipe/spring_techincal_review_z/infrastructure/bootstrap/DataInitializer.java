package com.felipe.spring_techincal_review_z.infrastructure.bootstrap;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Initializes the H2 in-memory database with schema and sample data on application startup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ConnectionFactory connectionFactory;

    @Override
    public void run(String... args) {
        log.info("Starting database initialization...");
        
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.addScript(new ClassPathResource("data.sql"));
            
            populator.populate(connectionFactory).block();
            
            log.info("Database initialization completed successfully - Schema and sample data loaded");
        } catch (Exception e) {
            log.error("Failed to initialize database: {}", e.getMessage(), e);
            throw new IllegalStateException("Database initialization failed", e);
        }
    }
}