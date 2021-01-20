package femi.core.utils.email;

import femi.core.utils.EnvironmentUtils;
import femi.pages_with_steps_definitions.email.Email;
import femi.pages_with_steps_definitions.email.EmailBox;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;


public class MailReader {
    private static final int WAIT_TIME = 5000; // 1min = 60 seconds * 1000 ms/sec
    private String emailAddress;
    private String password;

    static Logger LOGGER = LoggerFactory.getLogger(MailReader.class);

    public MailReader(String emailBox) throws Exception {
        retrieveEmailBoxDetails(emailBox);
    }

    public MailReader(EmailBox emailBox) throws Exception {
        retrieveEmailBoxDetails(emailBox);
    }

    private void retrieveEmailBoxDetails(String emailBox) {
        String emailBoxDetails = EnvironmentUtils.getEnvironmentDependentValue(emailBox);
        String[] details = emailBoxDetails.split("\\s+");
        emailAddress = details[1];
        password = details[2];
    }

    private void retrieveEmailBoxDetails(EmailBox emailBox) {
        emailAddress = emailBox.getEmailAddress();
        password = emailBox.getPassword();
    }

    //just unread  email
    public String readEmailBoxAndGetLastEmailBySubject(String subj, int min) {
        return waitEmailMinute(subj, min);
    }


    //read and unread email
    public String readAnyEmailBoxAndGetLastEmailBySubject(String subj, int min) {
        return waitAnyEmailMinute(subj, min);
    }


    public void deleteAllEmails() {
        Properties props = new Properties();
        String emailBody = "";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
            props.load(inputStream);
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect(props.getProperty("mail.smtp.host"), emailAddress, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);

            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                String subject = message.getSubject();
                message.setFlag(Flags.Flag.DELETED, true);
                System.out.println("Marked DELETE for message: " + subject);

            }
            // expunges the folder to remove messages which are marked deleted
            boolean expunge = true;
            folderInbox.close(expunge);

            // another way:
            //folderInbox.expunge();
            //folderInbox.close(false);

            // disconnect
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store.");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readEmailBoxAndGetLastEmailBySubject(String subj) {
        Properties props = new Properties();
        String emailBody = "";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
            props.load(inputStream);
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect(props.getProperty("mail.smtp.host"), emailAddress, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Flags seenFlag = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seenFlag, false);

            Message[] messages = inbox.search(unseenFlagTerm);
            int messageCount = messages.length;
            LOGGER.info("Total Messages: " + messageCount);
            LOGGER.info("------------------------------");
            for (int i = messageCount - 1; i >= 0; i--) {
                if (messages[i].getSubject().equalsIgnoreCase(subj) && isDateIsToday(messages[i].getSentDate()) ||
                        messages[i].getSubject().matches(subj) && isDateIsToday(messages[i].getSentDate())) {
                    Object content = messages[i].getContent();
                    if (content instanceof MimeMultipart) {
                        MimeMultipart multipart = (MimeMultipart) content;
                        int numParts = multipart.getCount();
                        for (int count = 0; count < numParts; count++) {
                            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(count);
                            if (part.getContentType().contains("text/html"))
                                emailBody += Jsoup.parse((String) content).text();
                            else if (part.isMimeType("multipart/*"))
                                emailBody = IOUtils.toString(((MimeMultipart) part.getContent()).getBodyPart(0).getInputStream(), "UTF-8");
                        }
                    } else {
                        emailBody = content.toString();
                    }
                    LOGGER.info("Mail Subject: " + messages[i].getSubject());
                    LOGGER.info("Created: " + messages[i].getSentDate());
                    LOGGER.debug("body: " + emailBody);
                    break;
                }
            }
            inbox.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailBody;
    }

    private String readAnyEmailBoxAndGetLastEmailBySubject(String subj) {
        Properties props = new Properties();
        String emailBody = "";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("smtp.properties");
            props.load(inputStream);
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect(props.getProperty("mail.smtp.host"), emailAddress, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();
            int messageCount = messages.length;
            LOGGER.info("Total Messages: " + messageCount);
            LOGGER.info("------------------------------");
            for (int i = messageCount - 1; i >= 0; i--) {
                if (messages[i].getSubject().equalsIgnoreCase(subj) && isDateIsToday(messages[i].getSentDate()) ||
                        messages[i].getSubject().matches(subj) && isDateIsToday(messages[i].getSentDate())) {
                    Object content = messages[i].getContent();
                    if (content instanceof MimeMultipart) {
                        MimeMultipart multipart = (MimeMultipart) content;
                        int numParts = multipart.getCount();
                        for (int count = 0; count < numParts; count++) {
                            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(count);
                            if (part.getContentType().contains("text/html"))
                                emailBody += Jsoup.parse((String) content).text();
                            else if (part.isMimeType("multipart/*"))
                                emailBody = IOUtils.toString(((MimeMultipart) part.getContent()).getBodyPart(0).getInputStream(), "UTF-8");
                        }
                    } else {
                        emailBody = content.toString();
                    }
                    LOGGER.info("Mail Subject: " + messages[i].getSubject());
                    LOGGER.info("Created: " + messages[i].getSentDate());
                    LOGGER.debug("body: " + emailBody);
                    break;
                }
            }
            inbox.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailBody;
    }





    private String waitEmailMinute(String subj, int min) {

        int numberTry = (min * 60000) / WAIT_TIME;
        String emailBody = readEmailBoxAndGetLastEmailBySubject(subj);

        while (numberTry > 0) {
            if (!emailBody.isEmpty()) {
                break;
            }
            waitUntil(WAIT_TIME);
            emailBody = readEmailBoxAndGetLastEmailBySubject(subj);
            numberTry--;
        }
        if (emailBody.isEmpty()) {
            fail("Email with subject '" + subj + "' was not received after " +min + " min(s) to email: " + emailAddress);
        }
        return emailBody;
    }

    private String waitAnyEmailMinute(String subj, int min) {

        int numberTry = (min * 60000) / WAIT_TIME;
        String emailBody = readAnyEmailBoxAndGetLastEmailBySubject(subj);

        while (numberTry > 0) {
            if (!emailBody.isEmpty()) {
                break;
            }
            waitUntil(WAIT_TIME);
            emailBody = readAnyEmailBoxAndGetLastEmailBySubject(subj);
            numberTry--;
        }
        if (emailBody.isEmpty()) {
            fail("Email with subject '" + subj + "' was not received after " +min + " min(s) to email: " + emailAddress);
        }
        return emailBody;
    }


    public String setNameToEmail(String textFromEmail) {
        textFromEmail = textFromEmail.replace("<provider_patient>", EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderFirstName") +
                " " + EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderLastName"));
        textFromEmail = textFromEmail.replace("<patientName>", EnvironmentUtils.getEnvironmentDependentValue("firstName") +
                " " + EnvironmentUtils.getEnvironmentDependentValue("lastName"));
        textFromEmail = textFromEmail.replace("<customer>", EnvironmentUtils.getEnvironmentDependentValue("existingCustomerFirstName") +
                " " + EnvironmentUtils.getEnvironmentDependentValue("existingCustomerLastName"));
        textFromEmail = textFromEmail.replace("<provider>", EnvironmentUtils.getEnvironmentDependentValue("existingProviderFirstName") +
                " " + EnvironmentUtils.getEnvironmentDependentValue("existingProviderLastName"));
        textFromEmail = textFromEmail.replace("<tmpAdminFirstName>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminFirstName"));
        textFromEmail = textFromEmail.replace("<tmpAdminLastName>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminLastName"));
        textFromEmail = textFromEmail.replace("<adminOrg>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminOrganizationName"));
        textFromEmail = textFromEmail.replace("<AC_URL>", EnvironmentUtils.getEnvironmentDependentValue("AC_HE_URI"));
        textFromEmail = textFromEmail.replace("<firstTimeEmail>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminEmail"));
        textFromEmail = textFromEmail.replace("<provider_DoctorOnline>", EnvironmentUtils.getEnvironmentDependentValue("C2PProviderDoctorOnlineFirstName") +
                " " + EnvironmentUtils.getEnvironmentDependentValue("C2PProviderDoctorOnlineLastName"));

        return textFromEmail;
    }


    private boolean isDateIsToday(Date date) {
        DateTime today = new DateTime();
        DateTime date1 = new DateTime(date);
        Days days = Days.daysBetween(today, date1);
        return days.getDays() == 0 ? true : false;
    }

    private void waitUntil(long timeInMilliseconds) {
        synchronized (this) {
            try {
                LOGGER.info("Waiting a " + timeInMilliseconds + " milliseconds...");
                Thread.sleep(timeInMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
