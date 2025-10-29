package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.FocusGroupMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FocusGroupMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long focusGroupId;

    @NotNull
    private Long userProfileId;

    @NotNull
    @Size(max = 50)
    private String role;

    @NotNull
    @Size(max = 50)
    private String status;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

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

    public Long getFocusGroupId() {
        return focusGroupId;
    }

    public void setFocusGroupId(Long focusGroupId) {
        this.focusGroupId = focusGroupId;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FocusGroupMemberDTO)) {
            return false;
        }

        FocusGroupMemberDTO focusGroupMemberDTO = (FocusGroupMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, focusGroupMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "FocusGroupMemberDTO{" +
        "id=" + getId() +
        ", tenantId='" + getTenantId() + "'" +
        ", focusGroupId=" + getFocusGroupId() +
        ", userProfileId=" + getUserProfileId() +
        ", role='" + getRole() + "'" +
        ", status='" + getStatus() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
