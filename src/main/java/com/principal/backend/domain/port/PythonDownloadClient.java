package com.principal.backend.domain.port;

import com.principal.backend.domain.model.DownloadResult;

public interface PythonDownloadClient {

    DownloadResult download(String url);
}
