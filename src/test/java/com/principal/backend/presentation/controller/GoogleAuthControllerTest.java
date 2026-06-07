package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.GoogleAuthUseCase;
import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.Role;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.model.UserStatus;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoogleAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class GoogleAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoogleAuthUseCase googleAuthUseCase;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserRepository userRepository;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String EMAIL = "test@example.com";
    private static final String CODE = "valid-auth-code";

    @BeforeEach
    void setUpSecurityContext() {
        User user = new User(USER_ID, EMAIL, "password", Role.ROLE_USER, UserStatus.ACTIVE, Instant.now());

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void exchangeCode_WithValidJwt_Returns200() throws Exception {
        when(googleAuthUseCase.execute(eq(CODE), eq(USER_ID)))
                .thenReturn(new AuthResult(EMAIL, USER_ID.toString(), true));

        mockMvc.perform(post("/api/auth/google/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "valid-auth-code"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.message").value("Token almacenados correctamente"));
    }

    @Test
    void exchangeCode_WhenUseCaseFails_Returns200WithErrorMessage() throws Exception {
        when(googleAuthUseCase.execute(eq(CODE), eq(USER_ID)))
                .thenReturn(new AuthResult(null, null, false));

        mockMvc.perform(post("/api/auth/google/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "valid-auth-code"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Error intercambiando code"));
    }

    @Test
    void exchangeCode_WhenAuthContextIsNull_Returns401() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(post("/api/auth/google/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "some-code"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void exchangeCode_WhenPrincipalIsNotUser_Returns401() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not-a-user-object");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/api/auth/google/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "some-code"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void exchangeCode_VerifiesUseCaseInteraction() throws Exception {
        when(googleAuthUseCase.execute(any(), any()))
                .thenReturn(new AuthResult(EMAIL, USER_ID.toString(), true));

        mockMvc.perform(post("/api/auth/google/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "code": "valid-auth-code"
                                }
                                """));

        verify(googleAuthUseCase).execute(eq(CODE), eq(USER_ID));
    }
}
