package com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest;

import com.felipe.api.model.ErrorResponse;
import com.felipe.spring_techincal_review_z.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

/**
 * Global exception handler for REST endpoints.
 * Provides consistent error responses across the API.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handlePriceNotFoundException(
            PriceNotFoundException ex,
            ServerWebExchange exchange) {
        
        log.info("Price not found - Path: {}, Message: {}", 
                exchange.getRequest().getPath().value(), ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            ServerWebExchange exchange) {
        
        log.warn("Invalid request - Path: {}, Message: {}", 
                exchange.getRequest().getPath().value(), ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {
        
        log.error("Unexpected error - Path: {}", exchange.getRequest().getPath().value(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage("An unexpected error occurred");
        errorResponse.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(errorResponse);
    }
}

