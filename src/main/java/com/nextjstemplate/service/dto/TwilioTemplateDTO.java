package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * DTO for Twilio WhatsApp message templates.
 */
public class TwilioTemplateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotBlank(message = "Template name is required")
  private String name;

  @NotBlank(message = "Template content is required")
  private String content;

  @NotBlank(message = "Template category is required")
  private String category;

  private String language;

  private String status;

  private List<String> parameters;

  private ZonedDateTime createdAt;

  private ZonedDateTime updatedAt;

  private String tenantId;

  public TwilioTemplateDTO() {
  }

  public TwilioTemplateDTO(String name, String content, String category) {
    this.name = name;
    this.content = content;
    this.category = category;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getParameters() {
    return parameters;
  }

  public void setParameters(List<String> parameters) {
    this.parameters = parameters;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TwilioTemplateDTO that = (TwilioTemplateDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(content, that.content) &&
        Objects.equals(category, that.category) &&
        Objects.equals(language, that.language) &&
        Objects.equals(status, that.status) &&
        Objects.equals(parameters, that.parameters) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(tenantId, that.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, content, category, language, status, parameters, createdAt, updatedAt, tenantId);
  }

  @Override
  public String toString() {
    return "TwilioTemplateDTO{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", content='" + content + '\'' +
        ", category='" + category + '\'' +
        ", language='" + language + '\'' +
        ", status='" + status + '\'' +
        ", parameters=" + parameters +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", tenantId='" + tenantId + '\'' +
        '}';
  }
}

