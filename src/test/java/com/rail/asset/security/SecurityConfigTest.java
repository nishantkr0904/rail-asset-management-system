package com.rail.asset.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.rail.asset.domain.Asset;
import com.rail.asset.repository.AssetRepository;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SecurityConfigTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AssetRepository assetRepository;

    @BeforeEach
    void clean() {
        assetRepository.deleteAll();
    }

    @Test
    void viewerCanReadAssets() {
        assetRepository.save(sampleAsset("SEC-READ"));

        ResponseEntity<Asset[]> response = viewerClient().getForEntity(baseUrl("/api/assets"), Asset[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void managerCanCreateAsset() {
        ResponseEntity<Asset> response = managerClient().postForEntity(
            baseUrl("/api/assets"),
            jsonRequest(Map.of(
                "assetCode", "SEC-CRT",
                "name", "Security Created",
                "category", "INFRA",
                "status", "ACTIVE"
            )),
            Asset.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(assetRepository.findByAssetCode("SEC-CRT")).isPresent();
    }

    @Test
    void viewerCannotCreateAsset() {
        ResponseEntity<String> response = viewerClient().postForEntity(
            baseUrl("/api/assets"),
            jsonRequest(Map.of(
                "assetCode", "SEC-DENY",
                "name", "Denied",
                "category", "INFRA",
                "status", "ACTIVE"
            )),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(assetRepository.findByAssetCode("SEC-DENY")).isEmpty();
    }

    private Asset sampleAsset(String code) {
        return new Asset(
            null,
            code,
            "Sample",
            "TRACK",
            null,
            "Acme",
            "MDL",
            null,
            LocalDate.of(2020, 1, 1),
            "ACTIVE",
            "LOC",
            30,
            LocalDate.of(2024, 1, 1),
            new BigDecimal("1.00"),
            new BigDecimal("100.00"),
            null
        );
    }

    private TestRestTemplate viewerClient() {
        return restTemplate.withBasicAuth("viewer", "viewerPass!");
    }

    private TestRestTemplate managerClient() {
        return restTemplate.withBasicAuth("manager", "managerPass!");
    }

    private String baseUrl(String uri) {
        return URI.create("http://localhost:" + port + uri).toString();
    }

    private HttpEntity<Map<String, Object>> jsonRequest(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
