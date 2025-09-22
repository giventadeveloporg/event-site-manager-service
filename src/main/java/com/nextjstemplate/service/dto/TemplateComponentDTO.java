package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for WhatsApp message template components.
 */
public class TemplateComponentDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type;
  private String text;
  private String format;
  private List<TemplateParameterDTO> parameters;

  public TemplateComponentDTO() {
  }

  public TemplateComponentDTO(String type, String text) {
    this.type = type;
    this.text = text;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public List<TemplateParameterDTO> getParameters() {
    return parameters;
  }

  public void setParameters(List<TemplateParameterDTO> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TemplateComponentDTO that = (TemplateComponentDTO) o;
    return Objects.equals(type, that.type) &&
        Objects.equals(text, that.text) &&
        Objects.equals(format, that.format) &&
        Objects.equals(parameters, that.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, text, format, parameters);
  }

  @Override
  public String toString() {
    return "TemplateComponentDTO{" +
        "type='" + type + '\'' +
        ", text='" + text + '\'' +
        ", format='" + format + '\'' +
        ", parameters=" + parameters +
        '}';
  }
}
