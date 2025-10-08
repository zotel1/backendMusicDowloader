package com.principal.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/download")
public class DownloadController {

    @PostMapping
    public DownloadResponse handleDownload(@RequestBody DownloadRequest request){
        return downloadService.dowloadFromPython(request);
    }
}
