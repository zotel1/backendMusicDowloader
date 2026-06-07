package com.principal.backend.presentation.dto;

public class AuthCodeRequest {

    private String code;

    public AuthCodeRequest() {}

    public AuthCodeRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
