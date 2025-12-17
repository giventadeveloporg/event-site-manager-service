package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.ContactFormService;
import com.nextjstemplate.service.dto.ContactFormDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing contact form submissions.
 */
@RestController
@RequestMapping("/api/contact-form-email")
@Tag(name = "contact-form-resource", description = "Contact Form Email API")
public class ContactFormResource {

    private final Logger log = LoggerFactory.getLogger(ContactFormResource.class);

    private static final String ENTITY_NAME = "contactForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactFormService contactFormService;

    public ContactFormResource(ContactFormService contactFormService) {
        this.contactFormService = contactFormService;
    }

    /**
     * {@code POST  /api/contact-form-email/send} : Send a contact form email.
     *
     * @param contactFormDTO the contact form data
     * @param xTenantId the X-Tenant-ID header (optional - can also be provided via tenant query parameter or JWT token claim)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and response body.
     */
    @Operation(
        summary = "Send contact form email",
        description = "Sends an email from a contact form submission. The tenant ID can be provided via X-Tenant-ID header, tenant query parameter, or JWT token claim. If not provided, defaults to the default tenant.",
        parameters = {
            @Parameter(
                name = "X-Tenant-ID",
                description = "Tenant ID (optional - can also be provided via tenant query parameter or JWT token claim)",
                example = "tenant_demo_002",
                in = ParameterIn.HEADER,
                required = false,
                schema = @Schema(type = "string")
            ),
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
        }
    )
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendContactFormEmail(
        @Valid @RequestBody ContactFormDTO contactFormDTO,
        @Parameter(hidden = true) @RequestHeader(value = "X-Tenant-ID", required = false) String xTenantId
    ) {
        log.debug("REST request to send contact form email: {}", contactFormDTO);

        String tenantId = getTenantId();

        Map<String, Object> result = contactFormService.sendContactFormEmail(contactFormDTO, tenantId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get tenant ID from context.
     */
    private String getTenantId() {
        try {
            String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
            if (tenantId == null || tenantId.isEmpty()) {
                throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
            }
            return tenantId;
        } catch (Exception e) {
            log.warn("Could not get tenant ID from context: {}", e.getMessage());
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
    }
}
