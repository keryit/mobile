package femi.pages_with_steps_definitions.email;

import femi.core.custom_assertions.CustomAssertion;
import femi.core.utils.EnvironmentUtils;
import femi.core.utils.Locales;
import femi.core.utils.StringUtils;
import femi.core.utils.UserRoleType;
import femi.core.utils.email.MailReader;
import femi.core.utils.properties.PropertyUtils;
import femi.core.utils.report.AllureHelper;
import net.thucydides.core.steps.ScenarioSteps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yecht.Data;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;


public class EmailSteps extends ScenarioSteps {
    static Logger LOGGER = LoggerFactory.getLogger(EmailSteps.class);
    private String pathToPropertyFile;

    @Step
    public void checkEmailReceivedForCreatedAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForCreation(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailReceivedForCreatedDepartAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForAssignToDepart(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailReceivedForStartedAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForStarted(userRoleType, appType), 5);
    }

    @Step
    public void checkEmailReceivedForStartedSoonAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForStartedSoon(userRoleType, appType), 5);
    }

    @Step
    public void checkEmailReceivedForCreatedDueTomorrowAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForCreationDueTomorrow(userRoleType, appType), 5);
    }

    @Step
    public void checkEmailReceivedForCancelAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForCancel(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailReceivedForUpdateAppointment(UserRoleType userRoleType, String appType)  {
        checkEmail(getEmailDataForUpdate(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailAssignToProviderAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataAssignToProvider(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailReceivedForOTP(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForOTP(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailWithSummaryAppointment(UserRoleType userRoleType, String appType) {
        checkEmail(getEmailDataForSummary(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailYouAreLateProviderAppointment(UserRoleType userRoleType, String appType) {

        checkEmail(getEmailDataYouAreLate(userRoleType, appType), 5);
    }

    public String getLinkFromEmail(Email email, int min){

        String body = getEmailBodyFromAnyEmail(email, min);
        Document doc = Jsoup.parse(body);
        String link = doc.select("td > a").toString();
        Pattern linkPattern = Pattern.compile("href=\"([^\"]*)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        Matcher pageMatcher = linkPattern.matcher(link);

        if (pageMatcher.find()){
            LOGGER.info("The link from email is " + pageMatcher.group(1));
            return pageMatcher.group(1);
        }

        return "The link was nat found";
    }

    public String getLinkFromUnreadEmail(Email email, int min){

        String body = getEmailBody(email, min);
        Document doc = Jsoup.parse(body);
        String link = doc.select("td > a").toString();
        Pattern linkPattern = Pattern.compile("href=\"([^\"]*)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        Matcher pageMatcher = linkPattern.matcher(link);

        if (pageMatcher.find()){
            LOGGER.info("The link from email is " + pageMatcher.group(1));
            return pageMatcher.group(1);
        }

        return "The link was nat found";
    }

    // ============================================== Admin console =====================================================================


    @Step
    public String getTmpAdminPasswordFromEmail(UserRoleType userRoleType, String appType) {
        String body = getEmailBodyFromAnyEmail(getEmailDataAdminTemporaryPassword(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);
        String pass = doc.select("table:nth-of-type(4) > tbody:nth-of-type(1) > tr:nth-of-type(2)").text().replaceAll("[\\s\\u00a0]","").trim();
        LOGGER.info("The password from email is: " + pass);

        return pass;
    }

    @Step
    public String getTmpClinicianPasswordFromEmail(UserRoleType userRoleType, String appType) {
        String body = getEmailBodyFromAnyEmail(getEmailDataClinicianWelcome(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);
        String pass = doc.select("table:nth-of-type(4) > tbody:nth-of-type(1) > tr:nth-of-type(2)").text().replaceAll("[\\s\\u00a0]","").trim();
        LOGGER.info("The password from email is: " + pass);

        return pass;
    }

    @Step
    public String getTmpAdminResetPasswordByAdminFromEmail(UserRoleType userRoleType, String appType) {
        String body = getEmailBodyFromAnyEmail(getEmailDataResetPasswordByAdmin(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);
        String pass = doc.select("table:nth-child(2) > tbody > tr > td > table > tbody > tr:nth-child(2) > td:nth-child(2)").last().text().replaceAll("[\\s\\u00a0]","").trim();

        LOGGER.info("The password from email is: " + pass);
        AllureHelper.logResponseAsHTML("Subject- " + getEmailDataResetPasswordByAdmin(userRoleType, appType).getExpectedSubject(), body);

        return pass;
    }

    @Step
    public String getOtpCodeFromEmail(UserRoleType userRoleType, String appType){
        String body = getEmailBodyFromAnyEmail(getEmailDataAdminOtpCode(UserRoleType.TMP_ADMIN, appType), 1);
        Document doc = Jsoup.parse(body);
        String otp = doc.select("table:nth-child(2) > tbody > tr > td:nth-child(2)").text().replaceAll("[^-?0-9]+", "");

        LOGGER.info("OTP from email is: " + otp);

        return otp;
    }


    @Step
    public void checkWelcomeEmailForNewlyCreatedAdmin(UserRoleType userRoleType, String appType){

        String body = getEmailBody(getEmailDataAdminWelcome(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);

        //get part of from email body
        String name = doc.select("table:nth-of-type(2) > tbody > tr:nth-of-type(1) > td:nth-of-type(2)").text();
        String url = doc.select("table:nth-of-type(3) > tbody > tr > td:nth-of-type(2)").text();
        String signIn = doc.select("table:nth-child(3) > tbody > tr > td:nth-child(2) > b:nth-child(6)").text();
        String afterLogin = doc.select("table:nth-child(4) > tbody > tr:nth-child(3) > td:nth-child(2)").text();

        //expected parts from body
        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();
        String nameExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.welcome.name.body"));
        String urlExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.welcome.url.body"));
        String signInExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.welcome.signIn.body"));
        String afterLoginExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.welcome.afterLogin.body"));

        AllureHelper.logResponseAsHTML("Subject- " + getEmailDataAdminWelcome(userRoleType, appType).getExpectedSubject(), body);

        CustomAssertion.assertTrue(name.equals(nameExpected), "The email is not contains first name and last name");
        CustomAssertion.assertTrue(urlExpected.contains(url),  "Expected body is " + urlExpected + "\n but body from email is " + url);
        CustomAssertion.assertTrue(signInExpected.contains(signIn), "The email is not contains signIn info");
        CustomAssertion.assertTrue(afterLoginExpected.equals(afterLogin), "The email is not contains afterLogin info");

    }

    @Step
    public void checkWelcomeEmailForNewlyCreatedClinician(UserRoleType userRoleType, String appType){

        String body = getEmailBody(getEmailDataClinicianWelcome(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);

        //get part of from email body
        String name = doc.select("table:nth-child(2) > tbody > tr:nth-child(1) > td:nth-child(2)").text();
        String url = doc.select("table:nth-child(2) > tbody > tr > td > table:nth-child(3) > tbody > tr > td:nth-child(2)").text();
        String signIn = doc.select("table:nth-child(3) > tbody > tr > td:nth-child(2) > b:nth-child(6)").text();
        String afterLogin = doc.select("table:nth-child(4) > tbody > tr:nth-child(3) > td:nth-child(2)").text();

        //expected parts from body
        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();
        String nameExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.clinician.welcome.name.body"));
        String urlExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.clinician.welcome.url.body"));
        String signInExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.clinician.welcome.signIn.body"));
        String afterLoginExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.clinician.welcome.afterLogin.body"));

        AllureHelper.logResponseAsHTML("Subject- " + getEmailDataAdminWelcome(userRoleType, appType).getExpectedSubject(), body);

        CustomAssertion.assertTrue(name.equals(nameExpected), "The email is not contains first name and last name");
        CustomAssertion.assertTrue(StringUtils.isEmailContainsText(url, urlExpected),  "Expected body is " + urlExpected + "\n but body from email is " + url);
        CustomAssertion.assertTrue(signInExpected.contains(signIn), "The email is not contains signIn info");
        CustomAssertion.assertTrue(afterLoginExpected.equals(afterLogin), "The email is not contains afterLogin info");

    }

    @Step
    public void checkEmailResetPasswordAdmin(UserRoleType userRoleType, String appType) {

        checkEmail(getEmailDataAdminResetPassword(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailOtpCodedAdmin(UserRoleType userRoleType, String appType) {

        checkEmail(getEmailDataAdminOtpCode(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailPasswordChangedAdmin(UserRoleType userRoleType, String appType) {

        checkEmail(getEmailDataAdminPasswordChanged(userRoleType, appType), 1);
    }

    @Step
    public void checkEmailResetPasswordByAdmin(UserRoleType userRoleType, String appType){

        String welcomeExpected = "";
        String afterLoginExpected = "";

        String body = getEmailBody(getEmailDataResetPasswordByAdmin(userRoleType, appType), 1);
        Document doc = Jsoup.parse(body);

        //get part of from email body
        String welcomeMessage = doc.select("tbody:nth-child(1) tr:nth-child(2) > td:nth-child(2)").text();
        String afterLogin = doc.select("tbody:nth-child(1) tr:nth-child(3) > td:nth-child(2)").text();

        //expected parts from body
        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        if (userRoleType==UserRoleType.TMP_ADMIN) {
            welcomeExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.reset.password.byAdmin.welcomeMessage.body"));
            afterLoginExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.reset.password.byAdmin.afterLogin.body"));
        }
        if (userRoleType==UserRoleType.TMP_CLINICIAN) {
            welcomeExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.reset.password.byAdmin.welcomeMessage.clinician.body"));
            afterLoginExpected = StringUtils.setParametersToAdmin(notifProper.getProperty("he.reset.password.byAdmin.afterLogin.clinician.body"));
        }

            AllureHelper.logResponseAsHTML("Subject- " + getEmailDataResetPasswordByAdmin(userRoleType, appType).getExpectedSubject(), body);

            CustomAssertion.assertTrue(StringUtils.isEmailContainsText(welcomeMessage, welcomeExpected), "The email is not contains welcome message " + welcomeMessage + "\n" + welcomeExpected);
            CustomAssertion.assertTrue(afterLoginExpected.equals(afterLogin), "The email is not contains afterLogin info");


    }

    // ============================================= General =====================================================================

    public void cleanEmailBox(UserRoleType userRoleType, String appType) {
        cleanEmailBox(getEmailDataForCancel(userRoleType, appType));
    }

    private void cleanEmailBox(Email email) {
        try {
            MailReader mailReader = new MailReader(email.getEmailBox());
            mailReader.deleteAllEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkEmail(Email email, int min) {
        try {
            MailReader mailReader = new MailReader(email.getEmailBox());
            String receivedEmailBody = mailReader.readEmailBoxAndGetLastEmailBySubject(mailReader.setNameToEmail(email.getExpectedSubject()), min);
            LOGGER.info(">>>>>>>>>>>" + mailReader.setNameToEmail(email.getExpectedEmailBody()));

            Assert.assertTrue("Email body is incorrect. Please check email: " + email.getEmailRecipient(),
                    StringUtils.isEmailContainsText(receivedEmailBody, mailReader.setNameToEmail(email.getExpectedEmailBody()).trim()));
            AllureHelper.logResponseAsHTML("Subject-" + mailReader.setNameToEmail(email.getExpectedSubject()), receivedEmailBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmailBody(Email email, int min) {
        MailReader mailReader = null;
        String emailBody = "";
        try {
            mailReader = new MailReader(email.getEmailBox());
            emailBody = mailReader.readEmailBoxAndGetLastEmailBySubject(mailReader.setNameToEmail(email.getExpectedSubject()), min);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(">>>>>>>>>>>" + mailReader.setNameToEmail(email.getExpectedEmailBody()));
        AllureHelper.logResponseAsHTML("Subject-" + mailReader.setNameToEmail(email.getExpectedSubject()), emailBody );
        if (emailBody.isEmpty()) {
            fail("Email with subject '" + mailReader.setNameToEmail(email.getExpectedSubject() + "' was not received after " +min + " min(s) to email: " + email));
        }

        return emailBody;
    }

    public String getEmailBodyFromAnyEmail(Email email, int min) {
        MailReader mailReader = null;
        String emailBody = "";
        try {
            mailReader = new MailReader(email.getEmailBox());
             emailBody = mailReader.readAnyEmailBoxAndGetLastEmailBySubject(mailReader.setNameToEmail(email.getExpectedSubject()), min);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(">>>>>>>>>>>" + mailReader.setNameToEmail(email.getExpectedEmailBody()));
        AllureHelper.logResponseAsHTML("Subject-" + mailReader.setNameToEmail(email.getExpectedSubject()), emailBody );
        if (emailBody.isEmpty()) {
            fail("Email with subject '" + mailReader.setNameToEmail(email.getExpectedSubject() + "' was not received after " +min + " min(s) to email: " + email));
        }

        return emailBody;
    }


    public Email getEmailDataForCancel(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.cancel.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.cancel.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.cancel.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.cancel.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForUpdate(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.update.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.update.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.update.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.update.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForUpdateDoctorOnline(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.update.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.update.appointment.doctor.online.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.update.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.update.appointment.doctor.online.body"));
        }
        return email;
    }


    public Email getEmailDataForCreation(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.create.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.create.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.create.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.create.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForCreationDoctorOnline(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.create.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.create.appointment.doctor.online.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.create.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.create.appointment.doctor.online.body"));
        }
        return email;
    }


    private Email getEmailDataForAssignToDepart(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.createDepart.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.createDepart.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.createDepart.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.createDepart.appointment.body"));
        }
        return email;
    }

    private Email getEmailDataAssignToProvider(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.assignProvider.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.assignProvider.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.assignProvider.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.assignProvider.appointment.body"));
        }
        return email;
    }

    private Email getEmailDataForSummary(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.summary.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.summary.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.summary.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.summary.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForStarted(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.started.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.started.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.started.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.started.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForStartedDoctorOnline(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.started.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.started.appointment.doctor.online.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.started.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.started.appointment.doctor.online.body"));
        }
        return email;
    }

    public Email getEmailDataForStartedSoon(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.started.soon.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.started.soon.appointment.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.started.soon.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.started.soon.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForStartedSoonDoctorOnline(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.started.soon.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.started.soon.appointment.doctor.online.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.started.soon.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.started.soon.appointment.doctor.online.body"));
        }
        return email;
    }



    public Email getEmailDataForCreationDueTomorrow(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.create.tomorrow.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.create.tomorrow.appointment.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.create.tomorrow.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.create.tomorrow.appointment.body"));
        }
        return email;
    }

    public Email getEmailDataForCreationDueTomorrowDoctorOnline(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.create.tomorrow.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.create.tomorrow.appointment.doctor.online.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.create.tomorrow.appointment.doctor.online.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.create.tomorrow.appointment.doctor.online.body"));
        }
        return email;
    }

    public Email getEmailDataYouAreLate(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.late.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.late.appointment.body"));
                break;
            case "PatientPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.late.appointment.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.late.appointment.body"));
        }
        return email;
    }

    private Email getEmailDataForOTP(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "PatientPortalEnglishLocale":
            case "ClinicianPortalEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.otp.code.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.otp.code.body"));
                break;
            case "PatientPortalHELocale":
            case "ClinicianPortalHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.otp.code.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.otp.code.body"));
        }
        return email;
    }

    private Email getEmailDataAdminTemporaryPassword(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.temporary.password.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.temporary.password.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.temporary.password.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.temporary.password.body"));
        }
        return email;
    }


    private Email getEmailDataAdminWelcome(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.welcome.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.welcome.welcomeMessage.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.welcome.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.welcome.welcomeMessage.body"));
        }
        return email;
    }

    public Email getEmailDataClinicianWelcome(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.clinician.welcome.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.clinician.welcome.welcomeMessage.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.clinician.welcome.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.clinician.welcome.welcomeMessage.body"));
        }
        return email;
    }

    public Email getEmailDataResetPasswordByAdmin(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        if (userRoleType==UserRoleType.TMP_ADMIN) {
            switch (appType) {
                case "AdminConsoleEnglishLocale":
                    email.setExpectedSubject(notifProper.getProperty("en.reset.password.byAdmin.subject"));
                    email.setExpectedEmailBody(notifProper.getProperty("en.reset.password.byAdmin.welcomeMessage.body"));
                    break;
                case "AdminConsoleHELocale":
                default:
                    email.setExpectedSubject(notifProper.getProperty("he.reset.password.byAdmin.subject"));
                    email.setExpectedEmailBody(notifProper.getProperty("he.reset.password.byAdmin.welcomeMessage.body"));
            }
        }
        if (userRoleType==UserRoleType.TMP_CLINICIAN) {
            switch (appType) {
                case "AdminConsoleEnglishLocale":
                    email.setExpectedSubject(notifProper.getProperty("en.reset.password.byAdmin.clinician.subject"));
                    email.setExpectedEmailBody(notifProper.getProperty("en.reset.password.byAdmin.welcomeMessage.clinician.body"));
                    break;
                case "AdminConsoleHELocale":
                default:
                    email.setExpectedSubject(notifProper.getProperty("he.reset.password.byAdmin.clinician.subject"));
                    email.setExpectedEmailBody(notifProper.getProperty("he.reset.password.byAdmin.welcomeMessage.clinician.body"));
            }
        }
        return email;
    }

    public Email getEmailDataAdminResetPassword(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.reset.password.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.reset.password.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.reset.password.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.reset.password.body"));
        }
        return email;
    }

    public Email getEmailDataAdminOtpCode(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.otp.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.otp.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.otp.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.otp.body"));
        }
        return email;
    }

    public Email getEmailDataAdminPasswordChanged(UserRoleType userRoleType, String appType) {
        Email email = new Email();
        email.setEmailBox(getEmailBoxAndSetPathToPropertyByRole(userRoleType));
        email.setEmailRecipient(email.getEmailBox().getEmailAddress());

        PropertyUtils notifications = new PropertyUtils(pathToPropertyFile);
        Properties notifProper = notifications.getPropValues();

        switch (appType) {
            case "AdminConsoleEnglishLocale":
                email.setExpectedSubject(notifProper.getProperty("en.password.changed.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("en.password.changed.body"));
                break;
            case "AdminConsoleHELocale":
            default:
                email.setExpectedSubject(notifProper.getProperty("he.password.changed.subject"));
                email.setExpectedEmailBody(notifProper.getProperty("he.password.changed.body"));
        }
        return email;
    }

    private EmailBox getEmailBoxAndSetPathToPropertyByRole(UserRoleType userRoleType) {
        EmailBox emailBox = new EmailBox();
        String emailBoxType = "";
        switch (userRoleType) {
            case CUSTOMER:
                emailBoxType = "clinicianCustomer";
                pathToPropertyFile = "text\\email_notifications_customer.properties";
                break;
            case PROVIDER:
                emailBoxType = "clinicianProvider";
                pathToPropertyFile = "text\\email_notifications_provider.properties";
                break;
            case PATIENT:
                emailBoxType = "patient";
                pathToPropertyFile = "text\\email_notifications_patient.properties";
                break;
            case C2P_PROVIDER:
            case C2P_PROVIDER_OTHER_ORG:
            case C2P_PROVIDER_DOCTOR_ONLINE:
                emailBoxType = "clinicianC2PProvider";
                pathToPropertyFile = "text\\email_notifications_patientProvider.properties";
                break;
            case TMP_ADMIN:
            case TMP_CLINICIAN:
                emailBoxType = "firstTimeLoginUser";
                pathToPropertyFile = "text\\email_notifications_admin.properties";
                break;
            case ADMIN:
                emailBoxType = "existingAdminEmail";
                pathToPropertyFile = "text\\email_notifications_admin.properties";
                break;
        }

        String emailBoxDetails = EnvironmentUtils.getEnvironmentDependentValue(emailBoxType);
        String[] details = emailBoxDetails.split("\\s+");
        emailBox.setEmailAddress(details[1]);
        emailBox.setPassword(details[2]);

        return emailBox;
    }

}
