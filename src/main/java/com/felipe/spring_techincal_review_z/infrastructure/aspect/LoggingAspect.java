package com.felipe.spring_techincal_review_z.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Logging aspect for domain services.
 * Keeps logging concerns out of the domain layer.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.felipe.spring_techincal_review_z.domain.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.debug("Executing domain service method: {} with args: {}", methodName, args);

        try {
            Object result = joinPoint.proceed();

            // Handle reactive types
            if (result instanceof Mono) {
                return ((Mono<?>) result)
                        .doOnNext(value -> log.info("Domain service {} completed successfully", methodName))
                        .doOnError(error -> log.error("Domain service {} failed: {}", methodName, error.getMessage(), error));
            }

            log.info("Domain service {} completed successfully", methodName);
            return result;

        } catch (Exception e) {
            log.error("Domain service {} failed: {}", methodName, e.getMessage(), e);
            throw e;
        }
    }
}