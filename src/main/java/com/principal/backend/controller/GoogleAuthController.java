package com.principal.backend.controller;


import com.principal.backend.dto.AuthCodeRequest;
import com.principal.backend.dto.AuthExchangeResponse;
import com.principal.backend.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/google")
@CrossOrigin(origins = "${app.frontend.origin:*}")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;
    private final String frontendOrigin;

    public GoogleAuthController(GoogleAuthService googleAuthService, @Value("${app.frontend.origin}") String frontendOrigin) {
        this.googleAuthService = googleAuthService;
        this.frontendOrigin = frontendOrigin;
    }

    @PostMapping("/exchange")
    public ResponseEntity<AuthExchangeResponse> exchangeCode(@RequestBody AuthCodeRequest request) {
        try {
            String email = googleAuthService.exchangeCodeAndStoreTokens(request.getCode());
            AuthExchangeResponse resp = new AuthExchangeResponse(email, "Token almacenados correctamente");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            AuthExchangeResponse resp = new AuthExchangeResponse(null, "Error intercambiando code: " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

}
