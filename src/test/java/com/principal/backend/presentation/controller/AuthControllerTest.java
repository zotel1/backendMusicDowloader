package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.LoginUserUseCase;
import com.principal.backend.application.usecase.RefreshTokenUseCase;
import com.principal.backend.application.usecase.RegisterUserUseCase;
import com.principal.backend.domain.exception.InvalidCredentialsException;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void register_Returns201() throws Exception {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        when(registerUserUseCase.execute("test@example.com", "password123"))
                .thenReturn(new RegisterUserUseCase.AuthResult(accessToken, refreshToken));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "password123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    void register_ValidationError_Returns400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "",
                                    "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_Returns200() throws Exception {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        when(loginUserUseCase.execute("test@example.com", "password123"))
                .thenReturn(new LoginUserUseCase.AuthResult(accessToken, refreshToken));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    void login_WrongCredentials_Returns401() throws Exception {
        when(loginUserUseCase.execute("test@example.com", "wrongPassword"))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "wrongPassword"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}
