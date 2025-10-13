package com.nextjstemplate.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.service.WebhookEventHandlerService;
import com.nextjstemplate.service.WebhookSignatureService;
import com.nextjstemplate.service.dto.ClerkWebhookRequest;
import com.nextjstemplate.service.dto.WebhookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for receiving Clerk webhook events.
 */
@RestController
@RequestMapping("/api/webhooks")
@Tag(name = "Clerk Webhooks", description = "Endpoints for receiving Clerk webhook events")
public class ClerkWebhookController {

    private final Logger log = LoggerFactory.getLogger(ClerkWebhookController.class);

    private final WebhookSignatureService webhookSignatureService;
    private final WebhookEventHandlerService webhookEventHandlerService;
    private final ObjectMapper objectMapper;

    public ClerkWebhookController(
        WebhookSignatureService webhookSignatureService,
        WebhookEventHandlerService webhookEventHandlerService,
        ObjectMapper objectMapper
    ) {
        this.webhookSignatureService = webhookSignatureService;
        this.webhookEventHandlerService = webhookEventHandlerService;
        this.objectMapper = objectMapper;
    }

    /**
     * POST /api/webhooks/clerk : Receive Clerk webhook events
     *
     * @param payload   the raw webhook payload
     * @param signature the X-Clerk-Signature header
     * @return the ResponseEntity with status 200 (OK) and processing result
     */
    @PostMapping("/clerk")
    @Operation(summary = "Receive Clerk webhook", description = "Endpoint for receiving webhook events from Clerk")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Webhook processed successfully",
                content = @Content(schema = @Schema(implementation = WebhookResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid signature"),
            @ApiResponse(responseCode = "400", description = "Invalid payload"),
        }
    )
    public ResponseEntity<WebhookResponse> handleClerkWebhook(
        @RequestBody String payload,
        @RequestHeader(value = "X-Clerk-Signature", required = false) String signature
    ) {
        long startTime = System.currentTimeMillis();
        log.info("Received Clerk webhook");

        WebhookResponse response = new WebhookResponse();

        try {
            // Verify signature
            if (signature == null || !webhookSignatureService.verifyWebhookSignature(payload, signature)) {
                log.warn("Invalid webhook signature");
                response.setSuccess(false);
                response.setMessage("Invalid signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Parse payload
            ClerkWebhookRequest webhookRequest = objectMapper.readValue(payload, ClerkWebhookRequest.class);

            log.info("Processing webhook event: {} (ID: {})", webhookRequest.getType(), webhookRequest.getId());

            // Check for duplicate/already processed events
            if (webhookEventHandlerService.isEventProcessed(webhookRequest.getId())) {
                log.info("Event {} already processed, skipping", webhookRequest.getId());
                response.setSuccess(true);
                response.setMessage("Event already processed");
                response.setEventId(webhookRequest.getId());
                response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
                return ResponseEntity.ok(response);
            }

            // Route to appropriate handler
            webhookEventHandlerService.handleEvent(webhookRequest);

            // Prepare success response
            response.setSuccess(true);
            response.setMessage("Webhook processed successfully");
            response.setEventId(webhookRequest.getId());
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);

            log.info("Webhook processed successfully in {}ms", response.getProcessingTimeMs());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            response.setSuccess(false);
            response.setMessage("Error processing webhook: " + e.getMessage());
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
