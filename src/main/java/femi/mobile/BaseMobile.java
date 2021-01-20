package femi.mobile;

import femi.core.utils.EnvironmentUtils;
import femi.core.utils.Locales;
import femi.core.utils.email.MailReader;
import femi.core.utils.helpers.http.Headers;
import femi.core.utils.helpers.http.HttpClient;
import femi.core.utils.helpers.postgre.PostgreDBHelper;
import femi.core.utils.report.AllureHelper;
import femi.mobile.mobileUtils.AppiumController;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

public class BaseMobile implements AppiumController {

    public static AppiumDriver driver;
    private File appDir = new File("src\\test\\application");
    static private Logger LOG = LoggerFactory.getLogger(BaseMobile.class);
    private String[] emails = {"patient", "clinicianC2PProvider"};

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        clearTestEmailBoxForTestUsers();
        markAsDeletedActiveConsultationsAndTerminateSessionForUser();
        markAsDeletedAppointmentsDB();

        setUpLocale();
    }

    @BeforeClass(alwaysRun = true)
    public void start() throws MalformedURLException {

        driver = setUpApp(OS.ANDROID,
                App.PATIENT_PORTAL,
                EnvironmentUtils.getEnvironmentDependentValue("APPIUM_SERVER_URL"),
                EnvironmentUtils.getEnvironmentDependentValue("APPIUM_SERVER_PORT"),
                EnvironmentUtils.getEnvironmentDependentValue("SAMSUNG_NOTE9_UDID"));

    }

    @AfterMethod(alwaysRun = true)
    public void collectTCResult(ITestResult result) throws Exception {
//        appiumController.collectTCResult(result);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(ITestContext testContext) {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public AppiumDriver getDriver() {
        if (this.driver == null || this.driver.getSessionId() == null)
            return null;
        return this.driver;
    }

    @Override
    public AppiumDriver setUpApp(OS executionOS, App appType, String URI, String port, String UDID) throws MalformedURLException {

        String appAndroid = null;
        String appIOS = null;

        if (appType == App.CLINICIAN_PORTAL) {
            appAndroid = "app-femi-debug1.apk";
            appIOS = "Clinician1.ipa";
        } else if (appType == App.PATIENT_PORTAL) {
            appAndroid = "Patient1.apk";
            appIOS = "Patient1.ipa";
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();

        switch (executionOS) {
            case ANDROID:
                assert appAndroid != null;
                File app = new File(appDir, appAndroid);
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("deviceName", "NotUsed");
                capabilities.setCapability("app", app.getAbsolutePath());
                capabilities.setCapability(MobileCapabilityType.UDID, UDID);
                driver = new AndroidDriver(new URL(EnvironmentUtils.getEnvironmentDependentValue("APPIUM_SERVER_URL") + ":" + port + "/wd/hub"), capabilities);
                break;
            case IOS:
                assert appIOS != null;
                app = new File(appDir, appIOS);
                capabilities.setCapability("platformName", "ios");
                capabilities.setCapability("app", app.getAbsolutePath());
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.UDID, UDID);
                driver = new IOSDriver(new URL(EnvironmentUtils.getEnvironmentDependentValue("APPIUM_SERVER_URL")
                        + ":" + port + "/wd/hub"), capabilities);
                break;
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }

    @Override
    public void removeApp(AppiumDriver driver, App appType) {
        String appAndroid = null;
        String appIOS = null;

        if (appType == App.CLINICIAN_PORTAL) {
            appAndroid = "com.femi.doctor1.femi";
            appIOS = "Clinician1.ipa";
        } else if (appType == App.PATIENT_PORTAL) {
            appAndroid = "com.femi.patient1.femi";
            appIOS = "com.femi.Patient1.femi";
        }
        try {
            if (driver instanceof AndroidDriver) {
                driver.removeApp(appAndroid);
            }
            if (driver instanceof IOSDriver) {
                driver.removeApp(appIOS);
            }
        } catch (ClassCastException ex) {

        }


    }

    private void setUpLocale() {
        setUpLocaleForClinicians(Locales.he);
    }

    private void setUpLocaleForClinicians(Locales locale) {
        PostgreDBHelper postgreDBHelper = new PostgreDBHelper();
        postgreDBHelper.setProperLocaleForUser(EnvironmentUtils.getEnvironmentDependentValue("existingCustomerEmail"), locale);
        postgreDBHelper.setProperLocaleById(EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderId"), locale);
        postgreDBHelper.setProperLocaleById(EnvironmentUtils.getEnvironmentDependentValue("existingPatientId"), locale);
    }

    private void markAsDeletedActiveConsultationsAndTerminateSessionForUser() {
        PostgreDBHelper postgreDBHelper = new PostgreDBHelper();
        postgreDBHelper.markAsDeletedActiveConsultationsAndTerminateSessionForUser(EnvironmentUtils.getEnvironmentDependentValue("existingCustomerEmail"));
        postgreDBHelper.markAsDeletedActiveConsultationsAndTerminateSessionForUser(EnvironmentUtils.getEnvironmentDependentValue("existingProviderEmail"));

    }

    private void markAsDeletedAppointmentsDB() {
        PostgreDBHelper postgreDBHelper = new PostgreDBHelper();

        postgreDBHelper.markAsDeletedCustomerAppointments(EnvironmentUtils.getEnvironmentDependentValue("existingCustomerEmail"));
        postgreDBHelper.markAsDeletedCustomerAppointments(EnvironmentUtils.getEnvironmentDependentValue("email"));
        postgreDBHelper.markAsDeletedCustomerAppointments(EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderEmail"));
        postgreDBHelper.markAsDeletedAppointments(EnvironmentUtils.getEnvironmentDependentValue("email"),"000000123");
        postgreDBHelper.markAsDeletedAppointments(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789");
        postgreDBHelper.markAsDeletedAppointments(EnvironmentUtils.getEnvironmentDependentValue("email"),"123456789123456");
        postgreDBHelper.deletePatientByPolicyIdFromDb(EnvironmentUtils.getEnvironmentDependentValue("email"), "000000123");
        postgreDBHelper.deletePatientByPolicyIdFromDb(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789");
        postgreDBHelper.deletePatientByPolicyIdFromDb(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789123456");
    }

    private void clearTestEmailBoxForTestUsers() {
        try {
            for (String emailBox : emails) {
                MailReader clinicianCustomerMailBox = new MailReader(emailBox);
                clinicianCustomerMailBox.deleteAllEmails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openApp() {
        driver.launchApp();
    }

    public String getResponseWithEmptyHeaders(String endPoint, String requestBody, String requestName) throws IOException {
        HttpClient httpClient = new HttpClient();
        AllureHelper.logRequestResponseAsXML(requestName + " XML Request:", requestBody);
        LOG.debug("REQUEST>>>>>" + requestBody);
        String response = httpClient.sendHTTPPostRequestAndGetResponse(endPoint, requestBody, new Headers());
        LOG.debug("RESPONSE>>>>>" + response);
        if (response == null || response.equals("")) {
            fail("Bad response was got from the server");
        }
        AllureHelper.logRequestResponseAsXML(requestName + " XML Response:", response);
        return response;
    }

}
