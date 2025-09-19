package com.nextjstemplate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service for generating JWT tokens for email subscription management.
 */
@Service
public class EmailSubscriptionTokenService {

  private final Logger log = LoggerFactory.getLogger(EmailSubscriptionTokenService.class);

  @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:86400}")
  private long tokenValidityInSeconds;

  private final JwtEncoder jwtEncoder;

  public EmailSubscriptionTokenService(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  /**
   * Generate a JWT token for email subscription management.
   * This token is used for unsubscribe links and email management operations.
   *
   * @param email    the user's email address
   * @param tenantId the tenant ID
   * @param userId   the user ID (optional, can be null for guest users)
   * @return the generated JWT token
   */
  public String generateEmailSubscriptionToken(String email, String tenantId, String userId) {
    log.debug("Generating email subscription token for email: {} and tenantId: {}", email, tenantId);

    Instant now = Instant.now();
    // Set a longer validity for email subscription tokens (30 days)
    Instant validity = now.plus(30, ChronoUnit.DAYS);

    JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet
        .builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(email) // Use email as the subject
        .claim("email", email)
        .claim("tenantId", tenantId)
        .claim("purpose", "email_subscription");

    // Add userId if provided
    if (userId != null && !userId.trim().isEmpty()) {
      claimsBuilder.claim("userId", userId);
    }

    JwtClaimsSet claims = claimsBuilder.build();

    JwsHeader jwsHeader = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS512).build();
    String token = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    log.debug("Successfully generated email subscription token for email: {}", email);
    return token;
  }

  /**
   * Generate a simple email subscription token using just email and tenantId.
   * This is a convenience method for the most common use case.
   *
   * @param email    the user's email address
   * @param tenantId the tenant ID
   * @return the generated JWT token
   */
  public String generateEmailSubscriptionToken(String email, String tenantId) {
    return generateEmailSubscriptionToken(email, tenantId, null);
  }
}
