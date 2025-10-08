package com.principal.backend.service;

import com.principal.backend.dto.DownloadRequest;
import com.principal.backend.dto.DownloadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DownloadService {
    private static final String PYTHON_API_URL = "http://127.0.0.1:8000/api/download/?url=";

    public DownloadResponse downloadFromPython(DownloadRequest request){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = PYTHON_API_URL + request.getUrl();
            ResponseEntity<DownloadResponse> response = restTemplate.getForEntity(requestUrl, DownloadResponse.class);

            return response.getBody();
        } catch (Exception e) {
            return new DownloadResponse("error", e.getMessage(), null);
        }
    }
}
