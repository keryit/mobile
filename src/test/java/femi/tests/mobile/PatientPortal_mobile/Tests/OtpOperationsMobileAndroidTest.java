package femi.tests.mobile.PatientPortal_mobile.Tests;

import femi.core.Data.SOAPData;
import femi.core.Data.SOAPOperations;
import femi.core.listeners.TestRailListener;
import femi.core.test_rail_client.annotation.TestRailCase;
import femi.core.utils.EnvironmentUtils;
import femi.core.utils.Locales;
import femi.data_provider.positive_c2p_soap.C2PCreationAppointmentDataProvider;
import femi.mobile.App;
import femi.mobile.BaseMobile;
import femi.mobile.PageObjectMobile.StepsMobile.loginStepsMobile.LoginStepsPatient;
import femi.mobile.PageObjectMobile.StepsMobile.mainScreenStepsMobile.MainScreenStepsPatient;
import femi.tests.main_test.TestListener;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.IOException;
import java.time.Duration;

@Listeners({TestListener.class, TestRailListener.class})
public class OtpOperationsMobileAndroidTest extends BaseMobile {

    private AppiumDriver driver;
    private SOAPOperations soapC2P = new SOAPOperations();

    private LoginStepsPatient loginStepsPatient;
    private MainScreenStepsPatient mainScreenStepsPatient;


    @Test(priority = 1, groups = {"acceptance", "regression"})
    @Title("Enter not numeric symbols to otp")
    public void enterNotNumericSymbolsToOtp() {

        driver = BaseMobile.driver;
        loginStepsPatient = new LoginStepsPatient(driver);
        mainScreenStepsPatient = new MainScreenStepsPatient(driver);

        loginStepsPatient.goToOtpPagePatient()
                .isOTPScreenOpened()
                .enterOtp("otpcode")
                .pressVerifyOnOtpScreenPatient()
                .isOtpFieldEmpty();
    }

    @Test(priority = 2, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - Wrong OTP can be entered as much as possible and user should not be blocked"})
    @Title("OTP- put wrong otp 6 times and than correct otp")
    public void putWrongOtp() {

        loginStepsPatient.isOTPScreenOpened()
                .enterOtp("123451")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOtp("123452")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOtp("123453")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOtp("123454")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOtp("123455")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOtp("123456")
                .pressVerifyOnOtpScreenPatient()
                .verifyErrorWrongOtp(Locales.he)
                .enterOTPByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressVerifyOnOtpScreenPatient();
        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal()
                .doSignOutPatientPortal();

        driver.closeApp();
    }

    @Test(priority = 3, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - OTP can be generated 5 times before blocking"})
    @Title("Enter OTP 5 times before blocking")
    public void otpLockDown() {

        driver.launchApp();

        loginStepsPatient.goToOtpPagePatient()
                .isOTPScreenOpened()
                .enterOtp("123451")
                .pressVerifyOnOtpScreenPatient()
                .isOTPScreenOpened();

        driver.navigate().back();

        loginStepsPatient.putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOtp("123452")
                .pressVerifyOnOtpScreenPatient()
                .isOTPScreenOpened();

        driver.navigate().back();

        loginStepsPatient.putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOtp("123453")
                .pressVerifyOnOtpScreenPatient()
                .isOTPScreenOpened();

        driver.navigate().back();

        loginStepsPatient.putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOtp("123454")
                .pressVerifyOnOtpScreenPatient()
                .isOTPScreenOpened();

        driver.navigate().back();

        loginStepsPatient.putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOtp("123455")
                .pressVerifyOnOtpScreenPatient()
                .isOTPScreenOpened();

        driver.navigate().back();


        loginStepsPatient.putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .verifyErrorThatUserLocked(Locales.he);

    }

    @Test(priority = 4, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA  - Sign in - User returns to block screen when refreshes or reopens blocked tab/app before block time passes"})
    @Title("Open app and verify that Lockdown screen opens")
    public void returnOnLockDownScreen() {

        try {
            driver.runAppInBackground(Duration.ofSeconds(3));
        } catch (Exception e) {
        }

        loginStepsPatient.verifyErrorThatUserLocked(Locales.he);
    }

    @Test(priority = 5, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - User is redirected to login page when block time ends"})
    @Title("User is redirected to login page when block time ends")
    public void redirectToLoginPageAfterUnlock() {

        driver.runAppInBackground(Duration.ofMinutes(5));

        loginStepsPatient
                .isLoginScreenOpened()
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressVerifyOnOtpScreenPatient();

        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal();

        driver.closeApp();

    }

    @Test(priority = 6, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentPatientPolicy123456789DoctorOnline", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 123456789")
    public void createAppointmentPatientPolicyId123456789DoctorOnlineTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 7, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - OTP can be generated 5 times before blocking"})
    @Title("Enter OTP 5 times before blocking Doctor online")
    public void otpLockDownDoctorOnline() {

        driver.launchApp();

        loginStepsPatient.isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenWithPhoneNumberOpened()
                .putPolicyId("123456789")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"));
        driver.hideKeyboard();
        loginStepsPatient
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOtp("123451")
                .pressSendOtpAgainBtnPatient();
        driver.runAppInBackground(Duration.ofMinutes(1));

        loginStepsPatient.pressSendOtpAgainBtnPatient();
        driver.runAppInBackground(Duration.ofMinutes(1));

        loginStepsPatient.pressSendOtpAgainBtnPatient();
        driver.runAppInBackground(Duration.ofMinutes(1));

        loginStepsPatient
                .pressSendOtpAgainBtnPatient();
        driver.runAppInBackground(Duration.ofMinutes(1));

        loginStepsPatient
                .pressSendOtpAgainBtnPatient();
        driver.runAppInBackground(Duration.ofMinutes(1));

        loginStepsPatient
                .pressSendOtpAgainBtnPatient();

        loginStepsPatient.verifyErrorThatUserLocked(Locales.he);

    }

    @Test(priority = 8, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA [MoH] - Sign in - User is redirected to login page with ID and Phone fields when block time ends"})
    @Title("User is redirected to login page when block time ends doctor online")
    public void redirectToLoginPageAfterUnlockDoctorOnline() {

        driver.runAppInBackground(Duration.ofMinutes(5));

        loginStepsPatient
                .isLoginScreenWithPhoneNumberOpened()
                .putPolicyId("123456789")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"));
        driver.hideKeyboard();
        loginStepsPatient
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789")
                .pressVerifyOnOtpScreenPatient();

        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal();

        driver.closeApp();

    }


    @Test(priority = 21, alwaysRun = true)
    @Title("Delete test patient with policy id 123456789")
    public void deletePatientDoctorOnlineFromDb() {

        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("123456789");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789");
    }


    @Test(priority = 22, dependsOnGroups = {"regression"})
    public void removeApp() {
        removeApp(driver, App.PATIENT_PORTAL);
    }
}
