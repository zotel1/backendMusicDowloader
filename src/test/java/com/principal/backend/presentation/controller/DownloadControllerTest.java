package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.DownloadUseCase;
import com.principal.backend.application.usecase.GetDownloadsUseCase;
import com.principal.backend.application.usecase.GetDownloadsUseCase.JobDetail;
import com.principal.backend.domain.model.*;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DownloadController.class)
@AutoConfigureMockMvc(addFilters = false)
class DownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DownloadUseCase downloadUseCase;

    @MockitoBean
    private GetDownloadsUseCase getDownloadsUseCase;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserRepository userRepository;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID JOB_ID = UUID.randomUUID();
    private static final String URL = "https://youtube.com/watch?v=test123";
    private static final Instant NOW = Instant.now();

    @BeforeEach
    void setUpSecurityContext() {
        User user = new User(USER_ID, "test@test.com", "pass", Role.ROLE_USER, UserStatus.ACTIVE, NOW);
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
    void handleDownload_WithValidUrl_ReturnsDownloadResponse() throws Exception {
        DownloadJob job = new DownloadJob(JOB_ID, USER_ID, DownloadStatus.PENDING, DownloadType.VIDEO,
                0, URL, null, NOW, NOW);
        when(downloadUseCase.execute(eq(USER_ID), eq(URL))).thenReturn(job);

        mockMvc.perform(post("/api/v1/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"url": "https://youtube.com/watch?v=test123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobId").value(JOB_ID.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.type").value("VIDEO"))
                .andExpect(jsonPath("$.progress").value(0));
    }

    @Test
    void listDownloads_ReturnsListOfDownloadResponses() throws Exception {
        DownloadJob job = new DownloadJob(JOB_ID, USER_ID, DownloadStatus.COMPLETED, DownloadType.AUDIO,
                100, URL, null, NOW, NOW);
        when(getDownloadsUseCase.getUserJobs(USER_ID)).thenReturn(List.of(job));

        mockMvc.perform(get("/api/v1/download"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobId").value(JOB_ID.toString()))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[0].type").value("AUDIO"));
    }

    @Test
    void getDownloadDetail_WithValidId_ReturnsJobDetailResponse() throws Exception {
        DownloadJob job = new DownloadJob(JOB_ID, USER_ID, DownloadStatus.DOWNLOADING, DownloadType.VIDEO,
                45, URL, null, NOW, NOW);
        MediaFile media = new MediaFile(UUID.randomUUID(), JOB_ID, "Test Title", "Test Artist",
                "Test Album", "3:30", "https://thumb.com/img.jpg", "Pop",
                "Test Channel", "2026-01-01", "drive-file-id-123");
        JobDetail detail = new JobDetail(job, media);
        when(getDownloadsUseCase.getJobDetail(USER_ID, JOB_ID)).thenReturn(detail);

        mockMvc.perform(get("/api/v1/download/{id}", JOB_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobId").value(JOB_ID.toString()))
                .andExpect(jsonPath("$.status").value("DOWNLOADING"))
                .andExpect(jsonPath("$.sourceUrl").value(URL))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.artist").value("Test Artist"))
                .andExpect(jsonPath("$.duration").value("3:30"));
    }
}
