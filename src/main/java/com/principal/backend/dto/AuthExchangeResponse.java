package com.principal.backend.dto;

public class AuthExchangeResponse {

    private String email;
    private String message;

    public AuthExchangeResponse() {}

    public AuthExchangeResponse(String email, String message){
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
