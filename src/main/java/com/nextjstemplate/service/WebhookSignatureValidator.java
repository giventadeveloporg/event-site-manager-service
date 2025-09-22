package com.nextjstemplate.service;

import com.twilio.security.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Service for validating Twilio webhook signatures.
 */
@Service
public class WebhookSignatureValidator {

  private static final Logger LOG = LoggerFactory.getLogger(WebhookSignatureValidator.class);

  /**
   * Validates the Twilio webhook signature.
   *
   * @param request      The HTTP request containing the webhook
   * @param webhookToken The webhook token for signature validation
   * @return true if the signature is valid, false otherwise
   */
  public boolean validateSignature(HttpServletRequest request, String webhookToken) {
    try {
      if (webhookToken == null || webhookToken.trim().isEmpty()) {
        LOG.warn("Webhook token is null or empty, cannot validate signature");
        return false;
      }

      RequestValidator validator = new RequestValidator(webhookToken);

      // Get the full URL
      String url = getFullUrl(request);

      // Get the signature from the request header
      String signature = request.getHeader("X-Twilio-Signature");
      if (signature == null || signature.trim().isEmpty()) {
        LOG.warn("Missing X-Twilio-Signature header");
        return false;
      }

      // Get the request body
      String body = getRequestBody(request);

      // Validate the signature
      boolean isValid = validator.validate(url, body, signature);

      if (isValid) {
        LOG.debug("Webhook signature validation successful");
      } else {
        LOG.warn("Webhook signature validation failed");
      }

      return isValid;

    } catch (Exception e) {
      LOG.error("Error validating webhook signature: {}", e.getMessage(), e);
      return false;
    }
  }

  /**
   * Validates the Twilio webhook signature using form data.
   *
   * @param request      The HTTP request containing the webhook
   * @param webhookToken The webhook token for signature validation
   * @param formData     The form data from the request
   * @return true if the signature is valid, false otherwise
   */
  public boolean validateSignature(HttpServletRequest request, String webhookToken, Map<String, String> formData) {
    try {
      if (webhookToken == null || webhookToken.trim().isEmpty()) {
        LOG.warn("Webhook token is null or empty, cannot validate signature");
        return false;
      }

      RequestValidator validator = new RequestValidator(webhookToken);

      // Get the full URL
      String url = getFullUrl(request);

      // Get the signature from the request header
      String signature = request.getHeader("X-Twilio-Signature");
      if (signature == null || signature.trim().isEmpty()) {
        LOG.warn("Missing X-Twilio-Signature header");
        return false;
      }

      // Validate the signature with form data
      boolean isValid = validator.validate(url, formData, signature);

      if (isValid) {
        LOG.debug("Webhook signature validation successful");
      } else {
        LOG.warn("Webhook signature validation failed");
      }

      return isValid;

    } catch (Exception e) {
      LOG.error("Error validating webhook signature: {}", e.getMessage(), e);
      return false;
    }
  }

  /**
   * Gets the full URL from the request.
   *
   * @param request The HTTP request
   * @return The full URL
   */
  private String getFullUrl(HttpServletRequest request) {
    StringBuffer requestURL = request.getRequestURL();
    String queryString = request.getQueryString();

    if (queryString == null) {
      return requestURL.toString();
    } else {
      return requestURL.append('?').append(queryString).toString();
    }
  }

  /**
   * Gets the request body from the request.
   * Note: This is a simplified implementation. In a real scenario,
   * you might need to handle different content types and character encodings.
   *
   * @param request The HTTP request
   * @return The request body as a string
   */
  private String getRequestBody(HttpServletRequest request) {
    try {
      // For form data, we'll reconstruct it from parameters
      StringBuilder body = new StringBuilder();
      Map<String, String[]> parameterMap = request.getParameterMap();

      boolean first = true;
      for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
        if (!first) {
          body.append("&");
        }
        body.append(entry.getKey()).append("=").append(entry.getValue()[0]);
        first = false;
      }

      return body.toString();
    } catch (Exception e) {
      LOG.error("Error reading request body: {}", e.getMessage(), e);
      return "";
    }
  }
}
