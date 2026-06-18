package com.ev.ready.ai.controller;

import com.ev.ready.ai.dto.AiRecommendationHealthResponse;
import com.ev.ready.ai.dto.AiRecommendationRequest;
import com.ev.ready.ai.service.AiRecommenderGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai/recommendations")
public class AiRecommendationController {

    private final AiRecommenderGatewayService aiRecommenderGatewayService;

    public AiRecommendationController(AiRecommenderGatewayService aiRecommenderGatewayService) {
        this.aiRecommenderGatewayService = aiRecommenderGatewayService;
    }

    @GetMapping("/health")
    public AiRecommendationHealthResponse getHealth() {
        return aiRecommenderGatewayService.getHealth();
    }

    @PostMapping
    public ResponseEntity<String> createRecommendation(@RequestBody AiRecommendationRequest request) {
        return aiRecommenderGatewayService.createRecommendation(request);
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<String> getRecommendation(@PathVariable Long id) {
        return aiRecommenderGatewayService.getRecommendation(id);
    }
}