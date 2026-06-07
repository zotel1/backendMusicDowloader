package com.principal.backend.domain.port;

import com.principal.backend.domain.model.AuthResult;

public interface GoogleAuthClient {

    AuthResult exchangeCode(String code);
}
