package com.felipe.spring_techincal_review_z;

import com.felipe.api.model.PriceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SpringTechincalReviewZApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void test1_requestAt10OnDay14() {
        // Test 1: request at 10:00 on the 14th for product 35455 for brand 1 (ZARA)
        // Expected: price = 35.50, priceList = 1
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getProductId()).isEqualTo(35455L);
                    assertThat(response.getBrandId()).isEqualTo(1L);
                    assertThat(response.getPriceList()).isEqualTo(1);
                    assertThat(response.getPrice()).isEqualTo(35.50);
                });
    }

    @Test
    void test2_requestAt16OnDay14() {
        // Test 2: request at 16:00 on the 14th for product 35455 for brand 1 (ZARA)
        // Expected: price = 25.45, priceList = 2 (higher priority)
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T16:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getProductId()).isEqualTo(35455L);
                    assertThat(response.getBrandId()).isEqualTo(1L);
                    assertThat(response.getPriceList()).isEqualTo(2);
                    assertThat(response.getPrice()).isEqualTo(25.45);
                });
    }

    @Test
    void test3_requestAt21OnDay14() {
        // Test 3: request at 21:00 on the 14th for product 35455 for brand 1 (ZARA)
        // Expected: price = 35.50, priceList = 1
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T21:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getProductId()).isEqualTo(35455L);
                    assertThat(response.getBrandId()).isEqualTo(1L);
                    assertThat(response.getPriceList()).isEqualTo(1);
                    assertThat(response.getPrice()).isEqualTo(35.50);
                });
    }

    @Test
    void test4_requestAt10OnDay15() {
        // Test 4: request at 10:00 on the 15th for product 35455 for brand 1 (ZARA)
        // Expected: price = 30.50, priceList = 3
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-15T10:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getProductId()).isEqualTo(35455L);
                    assertThat(response.getBrandId()).isEqualTo(1L);
                    assertThat(response.getPriceList()).isEqualTo(3);
                    assertThat(response.getPrice()).isEqualTo(30.50);
                });
    }

    @Test
    void test5_requestAt21OnDay16() {
        // Test 5: request at 21:00 on the 16th for product 35455 for brand 1 (ZARA)
        // Expected: price = 38.95, priceList = 4
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-16T21:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getProductId()).isEqualTo(35455L);
                    assertThat(response.getBrandId()).isEqualTo(1L);
                    assertThat(response.getPriceList()).isEqualTo(4);
                    assertThat(response.getPrice()).isEqualTo(38.95);
                });
    }

    @Test
    void testNotFound() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2021-01-01T10:00:00Z")
                        .queryParam("productId", 99999)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testInvalidProductId() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                        .queryParam("productId", -1)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testInvalidBrandId() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 0)
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testBoundaryStartDate() {
        // Test exactly at start date of price range
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T00:00:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getPriceList()).isEqualTo(1);
                    assertThat(response.getPrice()).isEqualTo(35.50);
                });
    }

    @Test
    void testBoundaryEndDate() {
        // Test exactly at end date of price range
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", "2020-06-14T18:30:00Z")
                        .queryParam("productId", 35455)
                        .queryParam("brandId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertThat(response.getPriceList()).isEqualTo(2);
                    assertThat(response.getPrice()).isEqualTo(25.45);
                });
    }

}
