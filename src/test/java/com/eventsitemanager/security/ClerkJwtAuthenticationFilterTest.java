package com.eventsitemanager.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eventsitemanager.repository.UserProfileRepository;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@ExtendWith(MockitoExtension.class)
class ClerkJwtAuthenticationFilterTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private UserProfileRepository userProfileRepository;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldMapAdminRoleFromAuthoritiesClaim() throws ServletException, IOException {
        ClerkJwtAuthenticationFilter filter = new ClerkJwtAuthenticationFilter(jwtDecoder, userProfileRepository);
        Jwt jwt = Jwt
            .withTokenValue("token")
            .header("alg", "HS256")
            .subject("jwtadmin")
            .claim("authorities", List.of("ROLE_ADMIN", "ROLE_USER"))
            .build();
        when(jwtDecoder.decode("token")).thenReturn(jwt);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("jwtadmin");
        assertThat(authentication.getAuthorities()).extracting("authority").contains("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void shouldAddFallbackAdminRoleForJwtAdminWithoutClaims() throws ServletException, IOException {
        ClerkJwtAuthenticationFilter filter = new ClerkJwtAuthenticationFilter(jwtDecoder, userProfileRepository);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "HS256").subject("jwtadmin").build();
        when(jwtDecoder.decode("token")).thenReturn(jwt);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities()).extracting("authority").contains("ROLE_ADMIN");
    }

    @Test
    void shouldNotGrantAdminToNonJwtAdminWhenClaimsMissing() throws ServletException, IOException {
        ClerkJwtAuthenticationFilter filter = new ClerkJwtAuthenticationFilter(jwtDecoder, userProfileRepository);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "HS256").subject("normal-user").build();
        when(jwtDecoder.decode("token")).thenReturn(jwt);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}
