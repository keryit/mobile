package femi.pages_with_steps_definitions.email;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Email {
    private String emailRecipient;
    private String emailSender;
    private String expectedSubject;
    @JsonProperty(value = "he.create.appointment.body")
    private String expectedEmailBody;
    private EmailBox emailBox;

    public Email() {
    }

    public EmailBox getEmailBox() {
        return emailBox;
    }

    public void setEmailBox(EmailBox emailBox) {
        this.emailBox = emailBox;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public String getExpectedSubject() {
        return expectedSubject;
    }

    public void setExpectedSubject(String expectedSubject) {
        this.expectedSubject = expectedSubject;
    }

    public String getExpectedEmailBody() {
        return expectedEmailBody;
    }

    public void setExpectedEmailBody(String expectedEmailBody) {
        this.expectedEmailBody = expectedEmailBody;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }
}
