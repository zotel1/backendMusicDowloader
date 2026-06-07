package com.principal.backend;

import com.principal.backend.application.port.PasswordEncoder;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class BackendApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private JwtProvider jwtProvider;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

}
