package com.principal.backend.infrastructure.adapter.python;

import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.domain.port.PythonDownloadClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PythonApiClientAdapter implements PythonDownloadClient {

    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public PythonApiClientAdapter(
            RestTemplate restTemplate,
            @Value("${python.api.url}") String pythonApiUrl) {
        this.restTemplate = restTemplate;
        this.pythonApiUrl = pythonApiUrl;
    }

    @Override
    public DownloadResult download(String url) {
        try {
            Map<String, String> requestBody = Map.of("url", url);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    pythonApiUrl + "/api/download/",
                    requestBody,
                    Map.class);

            if (response == null) {
                return new DownloadResult("error", "Python API returned null response", null);
            }

            String status = (String) response.getOrDefault("status", "error");
            String message = (String) response.getOrDefault("message", "");
            String filePath = (String) response.getOrDefault("file_path", null);

            return new DownloadResult(status, message, filePath);

        } catch (Exception e) {
            return new DownloadResult("error", e.getMessage(), null);
        }
    }
}
