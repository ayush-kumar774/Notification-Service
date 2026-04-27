package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.dto.GeminiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.base-url}")
    private String baseUrl;

    @Value("${gemini.model}")
    private String model;

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "geminiService", fallbackMethod = "fallback")
    public String generateMessage(String prompt) {

        String finalUrl = baseUrl.concat(model).concat(":generateContent?key=").concat(apiKey);

        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        GeminiResponse response = restTemplate.postForObject(finalUrl, request, GeminiResponse.class);

        if (response == null ||
                response.getCandidates() == null ||
                response.getCandidates().isEmpty() ||
                response.getCandidates().get(0).getContent() == null ||
                response.getCandidates().get(0).getContent().getParts() == null ||
                response.getCandidates().get(0).getContent().getParts().isEmpty()) {
            return prompt;
        }

        return response.getCandidates().get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }

    private String fallback(String prompt, Throwable t) {
        log.error("Gemini failed, using fallback", t);
        return prompt;
    }
}
