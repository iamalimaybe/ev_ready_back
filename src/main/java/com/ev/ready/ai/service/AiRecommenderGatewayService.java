package com.ev.ready.ai.service;

import com.ev.ready.ai.dto.AiRecommendationHealthResponse;
import com.ev.ready.ai.dto.AiRecommendationRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AiRecommenderGatewayService {

    private static final Logger log = LoggerFactory.getLogger(AiRecommenderGatewayService.class);

    private static final Pattern UP_STATUS_PATTERN =
            Pattern.compile("\"status\"\\s*:\\s*\"UP\"", Pattern.CASE_INSENSITIVE);

    private final RestClient restClient;

    public AiRecommenderGatewayService(
            @Qualifier("aiRecommenderRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public ResponseEntity<String> createRecommendation(AiRecommendationRequest request) {
        try {
            return restClient.post()
                    .uri("/api/v1/recommendations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(request)
                    .exchange((clientRequest, clientResponse) -> toGatewayResponse(clientResponse));
        } catch (ResourceAccessException ex) {
            log.warn("AI recommender service is unreachable while creating recommendation.");
            throw serviceUnavailable();
        } catch (RestClientException ex) {
            log.warn("AI recommender client error while creating recommendation.", ex);
            throw serviceUnavailable();
        }
    }

    public ResponseEntity<String> getRecommendation(Long id) {
        try {
            return restClient.get()
                    .uri("/api/v1/recommendations/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange((clientRequest, clientResponse) -> toGatewayResponse(clientResponse));
        } catch (ResourceAccessException ex) {
            log.warn("AI recommender service is unreachable while fetching recommendation {}.", id);
            throw serviceUnavailable();
        } catch (RestClientException ex) {
            log.warn("AI recommender client error while fetching recommendation {}.", id, ex);
            throw serviceUnavailable();
        }
    }

    public AiRecommendationHealthResponse getHealth() {
        try {
            return restClient.get()
                    .uri("/actuator/health")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange((clientRequest, clientResponse) -> toHealthResponse(clientResponse));
        } catch (Exception ex) {
            log.debug("AI recommender health check failed.");
            return AiRecommendationHealthResponse.down();
        }
    }

    private ResponseEntity<String> toGatewayResponse(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);

        if (statusCode.is2xxSuccessful() || statusCode.is4xxClientError()) {
            if (body == null || body.isBlank()) {
                if (statusCode.is4xxClientError()) {
                    throw new ResponseStatusException(statusCode, "AI recommender request failed.");
                }

                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "AI recommender service returned an empty response."
                );
            }

            return ResponseEntity.status(statusCode)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body);
        }

        if (statusCode.is5xxServerError()) {
            log.warn("AI recommender returned server error status {}.", statusCode.value());
            throw serviceUnavailable();
        }

        log.warn("AI recommender returned unexpected status {}.", statusCode.value());
        throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "AI recommender service returned an unexpected response."
        );
    }

    private AiRecommendationHealthResponse toHealthResponse(ClientHttpResponse response) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            return AiRecommendationHealthResponse.down();
        }

        String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);

        if (body == null || body.isBlank()) {
            return AiRecommendationHealthResponse.down();
        }

        if (UP_STATUS_PATTERN.matcher(body).find()) {
            return AiRecommendationHealthResponse.up();
        }

        return AiRecommendationHealthResponse.down();
    }

    private ResponseStatusException serviceUnavailable() {
        return new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "AI recommender service is unavailable."
        );
    }
}