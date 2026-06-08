package com.principal.backend.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoLinkedGoogleAccountExceptionTest {

    @Test
    void shouldHaveDefaultMessage() {
        NoLinkedGoogleAccountException exception = new NoLinkedGoogleAccountException();
        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        NoLinkedGoogleAccountException exception = new NoLinkedGoogleAccountException();
        assertInstanceOf(RuntimeException.class, exception);
    }
}
