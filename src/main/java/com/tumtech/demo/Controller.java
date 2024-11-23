package com.tumtech.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class Controller {
    private final NibssServices authService;

    @Autowired
    public Controller(NibssServices authService) {
        this.authService = authService;
    }

    @GetMapping("/generate-token")
    public ResponseEntity<?> generateToken() {
        try {
            String tokenResponse = authService.generateToken();
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/bvn/initiate")
    public ResponseEntity<?> initiateBvn(@RequestParam String bvnNumber, @RequestHeader("Authorization") String token) {
        try {
            String response = authService.initiateBvnValidation(bvnNumber, token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/bvn/otp")
    public ResponseEntity<?> validateOtp(@RequestBody Map<String, String> otpDetails, @RequestHeader("Authorization") String token) {
        try {
            String response = authService.validateOtp(otpDetails, token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/bvn/initiate/retry")
    public ResponseEntity<?> resendOtp(@RequestParam String bvnNumber, @RequestParam String receiver, @RequestHeader("Authorization") String token) {
        try {
            String response = authService.resendOtp(bvnNumber, receiver, token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }
}
