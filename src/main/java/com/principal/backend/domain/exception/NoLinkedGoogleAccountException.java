package com.principal.backend.domain.exception;

public class NoLinkedGoogleAccountException extends RuntimeException {

    public NoLinkedGoogleAccountException() {
        super("No Google account linked");
    }
}
