package com.principal.backend.presentation.dto;

public class DownloadResponse {
    private String status;
    private String message;
    private String title;

    // Creamos nuestro constructor vacio
    public DownloadResponse(){}

    public DownloadResponse(String status, String message, String title){
        this.status = status;
        this.message = message;
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
