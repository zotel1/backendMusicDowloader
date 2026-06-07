package com.principal.backend.domain.port;

import com.principal.backend.domain.model.GoogleTokenResponse;

public interface GoogleAuthClient {

    GoogleTokenResponse exchangeCode(String code);

    String refreshAccessToken(String refreshToken);
}
