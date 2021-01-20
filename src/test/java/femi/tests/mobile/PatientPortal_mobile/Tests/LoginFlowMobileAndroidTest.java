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

@Listeners({TestListener.class, TestRailListener.class})
public class LoginFlowMobileAndroidTest extends BaseMobile {


    private AppiumDriver driver;
    private SOAPOperations soapC2P = new SOAPOperations();

    private LoginStepsPatient loginStepsPatient;
    private MainScreenStepsPatient mainScreenStepsPatient;


    @Test(priority = 1, groups = {"acceptance", "regression"})
    @Title("Don't select organization press Next - User should stay on Select Organization screen")
    public void notSelectOrganization() {

        driver = BaseMobile.driver;
        loginStepsPatient = new LoginStepsPatient(driver);
        mainScreenStepsPatient = new MainScreenStepsPatient(driver);

        loginStepsPatient.isSelectOrganizationScreenOpened()
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isSelectOrganizationScreenOpened();
    }

    @Test(priority = 2, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - Verify User ID Number",
            "PA - Sign in - ID only - User ID is left empty"})
    @Title("User put empty and incorrect policyId and verify error")
    public void userEnterEmptyAndIncorrectPolicyId() {

        loginStepsPatient
                .selectOrganizationAndPressNextPatient()
                .isLoginScreenOpened()
                .pressNextOnPolicyIdScreen()
                .isLoginScreenOpened()
                .putPolicyId("123321")
                .pressNextOnPolicyIdScreen()
                .verifyErrorThanPolicyIdIncorrect(Locales.he);
    }

    @Test(priority = 3, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID only - User ID contains more than 9 chars"})
    @Title("Login with user policyId 12 symbols")
    public void loginWithCorrectPolicyIdMoreThan9Symbols() {

        loginStepsPatient
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened();
        driver.navigate().back();
    }

    @Test(priority = 4, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentForPatientWithPolicyId123456789", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 123456789")
    public void createAppointmentPatientPolicyId123456789Test(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 5, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID only - User ID contains 9 chars",
            "PA - First time log in - TOS/T&C page is shown after OTP submitting",
            "PA - First time log in - TOS are accepted"})
    @Title("Login with user policyId 9 symbols")
    public void loginWithCorrectPolicyI9Symbols() {

        loginStepsPatient
                .putPolicyId("123456789")
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears()
                .confirmTermsAndConditions();
        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal()
                .doSignOutPatientPortal();

        loginStepsPatient.isSelectOrganizationScreenOpened();
    }

    @Test(priority = 6, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentForPatientWithPolicyId000000123", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 123")
    public void createAppointmentPatientPolicyId123Test(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 7, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID only - User ID contains less than 9 chars",
            "PA - First time log in - TOS are declined completely (confirmed on the dialog)",
            "PA - First time log in - TOS are declined, but an action is not confirmed"})
    @Title("Login with user policyId less than 9 symbols")
    public void loginWithCorrectPolicyILessThan9Symbols() {

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationAndPressNextPatient()
                .isLoginScreenOpened()
                .putPolicyId("123")
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("000000123")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears()
                .declineTermsAndConditions()
                .waitDeclineTermsAndConditionsPopUp()
                .cancelDeclineTermsAndConditionsPopUp()
                .verifyTermsAndConditionsAppears()
                .declineTermsAndConditions()
                .waitDeclineTermsAndConditionsPopUp()
                .confirmDeclineTermsAndConditionsPopUp()
                .isSelectOrganizationScreenOpened()
                .selectOrganizationAndPressNextPatient()
                .isLoginScreenOpened()
                .putPolicyId("123")
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("000000123")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears()
                .confirmTermsAndConditions();
        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal();
        driver.closeApp();
    }

    @Test(priority = 8, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentForPatientWithPolicyId15Symbols", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 15 Symbols")
    public void createAppointmentPatientPolicyId15SymbolsTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 9, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID only - User ID contains 15 chars",
            "PA - Sign in - OTP validating"})
    @Title("Login with user policyId 15 symbols")
    public void loginWithCorrectPolicyI15Symbols() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationAndPressNextPatient()
                .isLoginScreenOpened()
                .putPolicyId("123456789123456")
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789123456")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears()
                .confirmTermsAndConditions();
        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal();

        driver.closeApp();
    }

    @Test(priority = 10, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentC2PDoctorOnline", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment Doctor Online for Patient Log in ID + phone")
    public void createAppointmentDoctorOnlineTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 11, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID and Phone number - Both fields are empty",
            "PA - Sign in - ID and Phone number - User enters incorrect value into ID field",
            "PA - Sign in - ID and Phone number - User enters incorrect value into phone field",
            "PA - Sign in - ID and Phone number - User enters only ID field",
            "PA - Sign in - ID and Phone number - User enters only phone field"})
    @Title("Login with user phone + policyId negative scenario")
    public void loginWithPhonePolicyNegativeScenario() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()

                //empty both fields
                .isLoginScreenWithPhoneNumberOpened()
                .pressNextOnPolicyIdScreen()
                .isLoginScreenWithPhoneNumberOpened()

                //put only ID
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen()
                .isLoginScreenWithPhoneNumberOpened();

        //put only Phone
        driver.navigate().back();
        loginStepsPatient.isSelectOrganizationScreenOpened()
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenWithPhoneNumberOpened()
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .isLoginScreenWithPhoneNumberOpened();

        //Incorrect ID correct Phone number
        loginStepsPatient.putPolicyId("55511155511")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isLoginScreenWithPhoneNumberOpened()

                //Correct ID and incorrect Phone
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .putPhoneNumber("01234587")
                .pressNextOnPolicyIdScreen()
                .isLoginScreenWithPhoneNumberOpened();

        driver.closeApp();
    }

    @Test(priority = 12, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID and Phone number - User ID contains more than 9 chars"})
    @Title("Login with user policyId 12 symbols")
    public void loginWithCorrectPolicyI12Symbols() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenOpened()
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressVerifyOnOtpScreenPatient();
        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal();

        driver.closeApp();
    }


    @Test(priority = 13, groups = {"acceptance", "regression"}, alwaysRun = true)
    @Title("Delete test patient with policy id 000000123, 123456789")
    public void deletePatientFromDb() {

        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("000000123");
        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("123456789");
        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("123456789123456");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "000000123");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789123456");
    }

    @Test(priority = 14, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentPatientPolicy123456789DoctorOnline", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 123456789")
    public void createAppointmentPatientPolicyId123456789DoctorOnlineTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 15, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID and Phone number - User ID contains 9 chars"})
    @Title("Login with user policyId 9 symbols")
    public void loginWithCorrectPolicyI9SymbolsDoctorOnline() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenOpened()
                .putPolicyId("123456789")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears();

        driver.closeApp();
    }

    @Test(priority = 16, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentPatientPolicy123DoctorOnline", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 123")
    public void createAppointmentPatientPolicyId123DoctorOnlineTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 17, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID and Phone number - User ID contains less than 9 chars"})
    @Title("Login with user policyId less than 9 symbols")
    public void loginWithCorrectPolicyILessThan9SymbolsDoctorOnline() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenOpened()
                .putPolicyId("123")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("000000123")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears();

        driver.closeApp();
    }

    @Test(priority = 18, groups = {"acceptance", "regression"}, dataProvider = "CreateAppointmentPatientPolicy15SymbolsDoctorOnline", dataProviderClass = C2PCreationAppointmentDataProvider.class)
    @Title("Create appointment for user with policyId 15 Symbols")
    public void createAppointmentPatientPolicyId15SymbolsDoctorOnlineTest(@Parameter("Description") String tcDescription, SOAPData c2pSOAPData, String expectedCreationResponse) throws IOException {

        //Create C2P appointment via SOAP
        String createAppointmentRequest = soapC2P.getPreparedCreateC2PAppointmentSOAPRequest(c2pSOAPData);
        String creationResponse = getResponseWithEmptyHeaders(EnvironmentUtils.getEnvironmentDependentValue("SOAP_ENDPOINT"), createAppointmentRequest, "Create: ");
        Assert.assertEquals(creationResponse, expectedCreationResponse, "Creation response is wrong.");
    }

    @Test(priority = 19, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - Sign in - ID and Phone number - User ID contains 15 chars"})
    @Title("Login with user policyId 15 symbols")
    public void loginWithCorrectPolicyI15SymbolsDoctorOnline() {

        driver.launchApp();

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenOpened()
                .putPolicyId("123456789123456")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789123456")
                .pressVerifyOnOtpScreenPatient()
                .verifyTermsAndConditionsAppears()
                .confirmTermsAndConditions();

        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal()
                .doSignOutPatientPortal();
    }

    @Test(priority = 20, groups = {"acceptance", "regression"})
    @TestRailCase(testCaseNames = {"PA - First time log in - TOS/T&C page is not shown after second login"})
    @Title("Login with user policyId 15 symbols")
    public void loginWithCorrectPolicyISecondTimeSymbolsDoctorOnline() {

        loginStepsPatient
                .isSelectOrganizationScreenOpened()
                .selectOrganizationPatient(EnvironmentUtils.getEnvironmentDependentValue("patientDoctorOnlineOrganizationName"))
                .pressBtnNextOnSelectOrganizationPatientPortal()
                .isLoginScreenOpened()
                .putPolicyId("123456789123456")
                .putPhoneNumber(EnvironmentUtils.getEnvironmentDependentValue("phoneNumberForLogin"))
                .pressNextOnPolicyIdScreen()
                .isOTPScreenOpened()
                .enterOTPByPolicyId("123456789123456")
                .pressVerifyOnOtpScreenPatient();

        mainScreenStepsPatient.isAppointmentsScreenOpenedPatientPortal()
                .doSignOutPatientPortal();

        driver.closeApp();
    }


    @Test(priority = 21, groups = {"acceptance", "regression"}, alwaysRun = true)
    @Title("Delete test patient with policy id 000000123, 123456789")
    public void deletePatientDoctorOnlineFromDb() {

        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("000000123");
        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("123456789");
        loginStepsPatient.deleteAppointmentsFromDbByPolicyId("123456789123456");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "000000123");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789");
        loginStepsPatient.deletePatientFromDbByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("email"), "123456789123456");
    }

    @Test(priority = 22, dependsOnGroups = {"regression"})
    public void removeApp() {
        removeApp(driver, App.PATIENT_PORTAL);
    }
}
