package com.felipe.spring_techincal_review_z.infrastructure.adapter.in.rest;

import com.felipe.api.model.ErrorResponse;
import com.felipe.spring_techincal_review_z.application.exception.PriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handlePriceNotFoundException(
            PriceNotFoundException ex,
            ServerWebExchange exchange) {
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(errorResponse);
    }
}

