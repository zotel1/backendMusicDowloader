package com.principal.backend;

import com.principal.backend.application.port.PasswordEncoder;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.GoogleAuthClient;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.DownloadJobJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.GoogleAccountJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.MediaFileJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.PlaylistJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.RefreshTokenJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
class BackendApplicationTests {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private JwtProvider jwtProvider;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@MockitoBean
	private GoogleAuthClient googleAuthClient;

	@MockitoBean
	private GoogleAccountRepository googleAccountRepository;

	@MockitoBean
	private UserJpaRepository userJpaRepository;

	@MockitoBean
	private RefreshTokenJpaRepository refreshTokenJpaRepository;

	@MockitoBean
	private GoogleAccountJpaRepository googleAccountJpaRepository;

	@MockitoBean
	private DownloadJobJpaRepository downloadJobJpaRepository;

	@MockitoBean
	private MediaFileJpaRepository mediaFileJpaRepository;

	@MockitoBean
	private PlaylistJpaRepository playlistJpaRepository;

	@Test
	void contextLoads() {
	}

}
