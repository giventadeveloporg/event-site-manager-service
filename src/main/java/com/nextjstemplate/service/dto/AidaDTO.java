package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.nextjstemplate.domain.BulkOperationLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AidaDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
