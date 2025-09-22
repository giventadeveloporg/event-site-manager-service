package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * DTO for bulk message recipients.
 */
public class BulkRecipientDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "Phone number is required")
  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in international format (e.g., +1234567890)")
  private String phoneNumber;

  private String name;

  private Map<String, String> personalizationParameters;

  public BulkRecipientDTO() {
  }

  public BulkRecipientDTO(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public BulkRecipientDTO(String phoneNumber, String name) {
    this.phoneNumber = phoneNumber;
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getPersonalizationParameters() {
    return personalizationParameters;
  }

  public void setPersonalizationParameters(Map<String, String> personalizationParameters) {
    this.personalizationParameters = personalizationParameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BulkRecipientDTO that = (BulkRecipientDTO) o;
    return Objects.equals(phoneNumber, that.phoneNumber) &&
        Objects.equals(name, that.name) &&
        Objects.equals(personalizationParameters, that.personalizationParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phoneNumber, name, personalizationParameters);
  }

  @Override
  public String toString() {
    return "BulkRecipientDTO{" +
        "phoneNumber='" + phoneNumber + '\'' +
        ", name='" + name + '\'' +
        ", personalizationParameters=" + personalizationParameters +
        '}';
  }
}
