package com.principal.backend.controller;

import com.principal.backend.dto.DownloadRequest;
import com.principal.backend.dto.DownloadResponse;
import com.principal.backend.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping
    public DownloadResponse handleDownload(@RequestBody DownloadRequest request){
        return downloadService.downloadFromPython(request);
    }
}
