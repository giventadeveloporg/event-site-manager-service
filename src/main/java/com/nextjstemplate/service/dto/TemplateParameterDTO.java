package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for WhatsApp message template parameters.
 */
public class TemplateParameterDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type;
  private String text;
  private String example;

  public TemplateParameterDTO() {
  }

  public TemplateParameterDTO(String type, String text) {
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

  public String getExample() {
    return example;
  }

  public void setExample(String example) {
    this.example = example;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TemplateParameterDTO that = (TemplateParameterDTO) o;
    return Objects.equals(type, that.type) &&
        Objects.equals(text, that.text) &&
        Objects.equals(example, that.example);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, text, example);
  }

  @Override
  public String toString() {
    return "TemplateParameterDTO{" +
        "type='" + type + '\'' +
        ", text='" + text + '\'' +
        ", example='" + example + '\'' +
        '}';
  }
}
