package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for contact form email submission.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactFormDTO implements Serializable {

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String lastName;

    @NotNull
    @NotBlank
    private String messageBody;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Email
    private String fromEmail;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Email
    private String toEmail;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactFormDTO)) {
            return false;
        }

        ContactFormDTO contactFormDTO = (ContactFormDTO) o;
        return (
            Objects.equals(this.firstName, contactFormDTO.firstName) &&
            Objects.equals(this.lastName, contactFormDTO.lastName) &&
            Objects.equals(this.fromEmail, contactFormDTO.fromEmail) &&
            Objects.equals(this.toEmail, contactFormDTO.toEmail)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.fromEmail, this.toEmail);
    }

    @Override
    public String toString() {
        return (
            "ContactFormDTO{" +
            "firstName='" +
            getFirstName() +
            "'" +
            ", lastName='" +
            getLastName() +
            "'" +
            ", fromEmail='" +
            getFromEmail() +
            "'" +
            ", toEmail='" +
            getToEmail() +
            "'" +
            "}"
        );
    }
}
