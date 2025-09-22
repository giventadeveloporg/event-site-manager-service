package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * DTO for WhatsApp message templates.
 */
public class MessageTemplateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String templateId;
  private String name;
  private String category;
  private String language;
  private String status;
  private String body;
  private List<TemplateComponentDTO> components;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  private String tenantId;

  public MessageTemplateDTO() {
  }

  public MessageTemplateDTO(String templateId, String name, String category, String language, String status) {
    this.templateId = templateId;
    this.name = name;
    this.category = category;
    this.language = language;
    this.status = status;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<TemplateComponentDTO> getComponents() {
    return components;
  }

  public void setComponents(List<TemplateComponentDTO> components) {
    this.components = components;
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

  public boolean isApproved() {
    return "APPROVED".equals(status);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MessageTemplateDTO that = (MessageTemplateDTO) o;
    return Objects.equals(templateId, that.templateId) &&
        Objects.equals(name, that.name) &&
        Objects.equals(category, that.category) &&
        Objects.equals(language, that.language) &&
        Objects.equals(status, that.status) &&
        Objects.equals(body, that.body) &&
        Objects.equals(components, that.components) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(tenantId, that.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, name, category, language, status, body, components, createdAt, updatedAt, tenantId);
  }

  @Override
  public String toString() {
    return "MessageTemplateDTO{" +
        "templateId='" + templateId + '\'' +
        ", name='" + name + '\'' +
        ", category='" + category + '\'' +
        ", language='" + language + '\'' +
        ", status='" + status + '\'' +
        ", body='" + body + '\'' +
        ", components=" + components +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", tenantId='" + tenantId + '\'' +
        '}';
  }
}
