package com.tumtech.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NibssServices {
    @Value("${nibss.clientId}")
    private String clientId;

    @Value("${nibss.clientSecret}")
    private String clientSecret;

    @Value("${nibss.apiUrl}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public NibssServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateToken() {
        String url = apiUrl + "/api/v1/auth/generate-token";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("clientId", clientId);
        requestBody.put("clientSecret", clientSecret);

        // Create HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the POST request
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return response; // Return raw response, parse if needed
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error calling NIBSS API: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred: " + ex.getMessage());
        }
    }

    public String initiateBvnValidation(String bvnNumber, String token) {
        String url = apiUrl + "/api/v1/validation/bvn/initiate";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create request body
        Map<String, String> requestBody = Map.of("bvnNumber", bvnNumber);

        // Create HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the GET request
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return response;
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error initiating BVN validation: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred: " + ex.getMessage());
        }
    }

    public String validateOtp(Map<String, String> otpDetails, String token) {
        String url = apiUrl + "/api/v1/validation/bvn/otp";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(otpDetails, headers);

        try {
            // Make the POST request
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return response;
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error validating OTP: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred: " + ex.getMessage());
        }
    }


    public String resendOtp(String bvnNumber, String receiver, String token) {
        String url = apiUrl + "/api/v1/validation/bvn/initiate/retry";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create request body
        Map<String, String> requestBody = Map.of(
                "bvnNumber", bvnNumber,
                "receiver", receiver
        );

        // Create HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the POST request
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return response;
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error resending OTP: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred: " + ex.getMessage());
        }
    }

}

