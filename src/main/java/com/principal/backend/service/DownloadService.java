package com.principal.backend.service;


import com.principal.backend.dto.DownloadRequest;
import com.principal.backend.dto.DownloadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class DownloadService {

    private static final String PYTHON_API_URL = "https://tu-python-api.up.railway.app/api/download/?url=";

    @Autowired
    private GoogleDriveService googleDriveService;

    public DownloadResponse handleDownloadProcess(DownloadRequest request) {
        try {
            // 1️⃣ Llamar a Python para descargar el archivo
            RestTemplate restTemplate = new RestTemplate();
            String pythonUrl = PYTHON_API_URL + URLEncoder.encode(request.getUrl(), StandardCharsets.UTF_8);

            ResponseEntity<Map> pythonResponse = restTemplate.getForEntity(pythonUrl, Map.class);

            if (pythonResponse.getBody() == null || !pythonResponse.getBody().containsKey("file_path")) {
                return new DownloadResponse("error", "Python no devolvió una ruta válida.", null);
            }

            String filePath = (String) pythonResponse.getBody().get("file_path");

            // 2️⃣ Subir el archivo al Drive del usuario
            File file = new File(filePath);
            String mimeType = Files.probeContentType(Paths.get(filePath));

            googleDriveService.uploadFileWithAccessToken(request.getAccessToken(), file, mimeType);

            // 3️⃣ Eliminar el archivo temporal
            Files.deleteIfExists(Paths.get(filePath));

            return new DownloadResponse("success", "Archivo descargado y subido correctamente.", file.getName());

        } catch (Exception e) {
            e.printStackTrace();
            return new DownloadResponse("error", e.getMessage(), null);
        }
    }
}


    /*
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
    }*/

