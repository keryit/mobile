package femi.mobile.PageObjectMobile.StepsMobile.loginStepsMobile;

import femi.core.custom_assertions.CustomAssertion;
import femi.core.utils.EnvironmentUtils;
import femi.core.utils.Locales;
import femi.core.utils.helpers.postgre.PostgreDBHelper;
import femi.core.utils.report.AllureHelper;
import femi.mobile.PageObjectMobile.PatientPortalApp.*;
import femi.pages_with_steps_definitions.PatientPortal.login.LoginSteps;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.thucydides.core.steps.ScenarioSteps;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.concurrent.TimeUnit;


public class LoginStepsPatient extends ScenarioSteps {

    private SelectOrganizationPatient selectOrganizationPatient;
    private LoginScreenPatient loginScreenPatient;
    private OTPScreenPatient otpScreenPatient;

    private AppiumDriver driver;
    private PostgreDBHelper postgreDBHelper = new PostgreDBHelper();

    public LoginStepsPatient(AppiumDriver driver) {
        this.driver = driver;
        if (driver instanceof AndroidDriver) {
            selectOrganizationPatient = new SelectOrganizationPatientAndroid(driver);
            loginScreenPatient = new LoginScreenPatientAndroid(driver);
            otpScreenPatient = new OTPScreenPatientAndroid(driver);
        } else if (driver instanceof IOSDriver) {
            selectOrganizationPatient = new SelectOrganizationPatientIOS(driver);
            loginScreenPatient = new LoginScreenPatientIOS(driver);
            otpScreenPatient = new OTPScreenPatientIOS(driver);
        }
    }


    // ========================================================== SELECT ORGANIZATION OPERATIONS ===================================================================================


    @Step
    public LoginStepsPatient isSelectOrganizationScreenOpened() {
        CustomAssertion.assertTrue(driver, selectOrganizationPatient.isOrganizationScreenOpened(), "Organization screen not Opened");
        AllureHelper.saveAllureScreenShot("Organization screen", driver);
        return this;
    }

    @Step
    public LoginStepsPatient pressBtnNextOnSelectOrganizationPatientPortal() {
        selectOrganizationPatient.clickNextButton();
        return this;
    }

    @Step
    public LoginStepsPatient selectOrganizationAndPressNextPatient() {
        selectOrganizationPatient.selectOrganization(EnvironmentUtils.getEnvironmentDependentValue("existingPatientOrganizationName"));
        selectOrganizationPatient.clickNextButton();
        return this;
    }

    @Step
    public LoginStepsPatient selectOrganizationPatient(String organizationName) {
        selectOrganizationPatient.selectOrganization(organizationName);
        return this;
    }

    // ========================================================== LOGIN SCREEN OPERATIONS ==========================================================================================


    @Step
    public LoginStepsPatient isLoginScreenOpened() {
        loginScreenPatient.isLoginScreenOpened();
        return this;
    }

    @Step
    public LoginStepsPatient isLoginScreenWithPhoneNumberOpened() {
        loginScreenPatient.isLoginScreenWithPhoneNumberOpened();
        return this;
    }

    @Step
    public LoginStepsPatient putPolicyId(String policyId) {
        loginScreenPatient.putPatientPolicyId(policyId);
        return this;
    }

    @Step
    public LoginStepsPatient isPolicyFieldEmpty() {
        if (!loginScreenPatient.getPolicyIdField().getAttribute("value").isEmpty()) {
            CustomAssertion.fail(driver, "Policy id field is not empty");
            AllureHelper.saveAllureScreenShot("PolicyId field", driver);
        }
        return this;
    }

    @Step
    public LoginStepsPatient putPhoneNumber(String phone) {
        loginScreenPatient.putPatientPhone(phone);
        return this;
    }

    @Step
    public LoginStepsPatient isPhoneNumberFieldEmpty() {
        if (!loginScreenPatient.getPatientPhoneField().getAttribute("value").isEmpty()) {
            CustomAssertion.fail(driver, "Phone number field is not empty");
            AllureHelper.saveAllureScreenShot("PhoneNumber field", driver);
        }
        return this;
    }

    @Step
    public LoginStepsPatient pressNextOnPolicyIdScreen() {
        loginScreenPatient.clickNextBtn();
        return this;
    }

    @Step
    public LoginStepsPatient pressBackBtnOnLoginScreenIOS(){
        LoginScreenPatientIOS login = new LoginScreenPatientIOS(driver);
        login.clickBackBtn();
        return this;
    }

    @Step
    public LoginStepsPatient verifyErrorThanPolicyIdIncorrect(Locales locale) {
        if (locale == Locales.en) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[@text='Your credentials are incorrect. Please check your credentials and try again']")).isDisplayed(),
                    "There is no error that Credentials incorrect on English");
        } else if (locale == Locales.he) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[@text='אנא בדקו את פרטי הכניסה שנית']")).isDisplayed(),
                    "There is no error that Credentials incorrect on Hebrew");
        }
        AllureHelper.saveAllureScreenShot("Credential incorrect", driver);
        return this;
    }

    @Step
    public LoginStepsPatient verifyTermsAndConditionsAppears() {
        Assert.assertTrue("Terms and Conditions was not loaded.", loginScreenPatient.isTermsAndConditionsFormAppears());
        AllureHelper.saveAllureScreenShot("Terms and Conditions form", driver);
        return this;
    }

    @Step
    public LoginStepsPatient waitDeclineTermsAndConditionsPopUp() {
        Assert.assertTrue("Decline Terms and Conditions pop up was not loaded.", loginScreenPatient.waitDeclineTermsAndConditionsPopUp());
        AllureHelper.saveAllureScreenShot("Decline pop up Terms and Conditions", driver);
        return this;
    }

    @Step
    public LoginStepsPatient confirmTermsAndConditions() {
        loginScreenPatient.confirmTermAndConditions();
        AllureHelper.saveAllureScreenShot("Confirm terms and Conditions", driver);
        return this;
    }

    @Step
    public LoginStepsPatient declineTermsAndConditions() {
        loginScreenPatient.declineTermsAndConditions();
        AllureHelper.saveAllureScreenShot("Decline terms and Conditions", driver);
        return this;
    }

    @Step
    public LoginStepsPatient confirmDeclineTermsAndConditionsPopUp() {
        loginScreenPatient.confirmDeclineTermsAndConditionOnPopUp();
        return this;
    }

    @Step
    public LoginStepsPatient cancelDeclineTermsAndConditionsPopUp() {
        loginScreenPatient.cancelDeclineTermsAndConditionsOnPopUp();
        return this;
    }

    // ========================================================= OTP SCREEN OPERATIONS =============================================================================================

    @Step
    public LoginStepsPatient enterOtp(String otpCode) {
        otpScreenPatient.enterOTP(otpCode);
        return this;
    }

    @Step
    public LoginStepsPatient enterOTPByPolicyId(String policyId) {
        String otp;
        PostgreDBHelper postgreDBHelper = new PostgreDBHelper();
        try {
            otp = postgreDBHelper.getValidOtp(policyId);
            otpScreenPatient.enterOTP(otp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;    }



    @Step
    public LoginStepsPatient isOTPScreenOpened() {
        otpScreenPatient.isOTPOpened();
        return this;
    }

    @Step
    public LoginStepsPatient isOtpFieldEmpty() {
        if (!otpScreenPatient.getOtp().getAttribute("value").isEmpty()) {
            CustomAssertion.fail(driver, "Otp field is not empty");
            AllureHelper.saveAllureScreenShot("Otp field", driver);

        }
        return this;
    }

    @Step
    public LoginStepsPatient isOtpFieldEmptyIOS() {
        if (!otpScreenPatient.getOtp().getAttribute("value").contains("OTP")) {
            CustomAssertion.fail(driver, "Otp field is not empty");
            AllureHelper.saveAllureScreenShot("Otp field", driver);

        }
        return this;
    }

    @Step
    public LoginStepsPatient pressSendOtpAgainBtnPatient(){
        otpScreenPatient.clickSendCodeAgain();
        return this;
    }

    @Step
    public LoginStepsPatient pressVerifyOnOtpScreenPatient() {
        otpScreenPatient.clickVerifyMeOnOtp();
        return this;
    }

    @Step
    public LoginStepsPatient pressBackBtnOnOtpScreenIOS(){
        OTPScreenPatientIOS otp = new OTPScreenPatientIOS(driver);
        otp.clickBackBtn();
        return this;
    }

    @Step
    public LoginStepsPatient goToOtpPagePatient() {
        isSelectOrganizationScreenOpened()
                .selectOrganizationAndPressNextPatient()
                .isLoginScreenOpened()
                .putPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"))
                .pressNextOnPolicyIdScreen();
        return this;
    }

    @Step
    public LoginStepsPatient verifyErrorWrongOtp(Locales locale) {
        if (locale == Locales.en) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[@text='The code you entered is incorrect. Please recheck and try again']")).isDisplayed(),
                    "There is no error about wrong otp on english");
        } else if (locale == Locales.he) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[@text='הקוד שהזנתם שגוי. אנא בדקו ונסו שוב']")).isDisplayed(),
                    "There is no error about wrong otp on hebrew");
        }
        AllureHelper.saveAllureScreenShot("Error about wrong otp", driver);
        return this;
    }

    @Step
    public LoginStepsPatient verifyErrorThatUserLocked(Locales locale) {
        AllureHelper.saveAllureScreenShot("Error about locked user on Otp page", driver);
        if (locale == Locales.en) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[contains(text(),'You exceeded the number of allowed attempts. Please wait')]")).isDisplayed(),
                    "Otp page - there is no error that user locked (English)");
        } else if (locale == Locales.he) {
            CustomAssertion.assertTrue(driver, driver.findElement(By.xpath("//*[contains(text(),'חרגתם ממספר הנסיונות המותר. אנא המתינו')]")).isDisplayed(),
                    "Otp page - there is no error that user locked (Hebrew)");
        }

        return this;
    }

    @Step
    public LoginStepsPatient clickTryAgainAfterUnlockOtpIOS(){
        driver.findElement(By.xpath("//*[@class='UIAButton']")).click();
        return this;
    }


    // =========================================================== GENERAL STEPS ===================================================================================================

    @Step
    public LoginStepsPatient loginAsPatient() {
        selectOrganizationPatient.isOrganizationScreenOpened();
        selectOrganizationPatient.selectOrganization(EnvironmentUtils.getEnvironmentDependentValue("existingPatientOrganizationName"));
        selectOrganizationPatient.clickNextButton();
        loginScreenPatient.isLoginScreenOpened();
        loginScreenPatient.putPatientPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"));
        loginScreenPatient.clickNextBtn();
        otpScreenPatient.isOTPOpened();
        enterOTPByPolicyId(EnvironmentUtils.getEnvironmentDependentValue("policyID"));
        otpScreenPatient.clickVerifyMeOnOtp();
        return this;
    }

    @Step
    public LoginStepsPatient deletePatientFromDbByPolicyId(String email, String policyId) {
        postgreDBHelper.deletePatientByPolicyIdFromDb(email, policyId);
        return this;
    }

    @Step
    public LoginStepsPatient deleteAppointmentsFromDbByPolicyId(String policyId) {
        postgreDBHelper.markAsDeletedAppointments(EnvironmentUtils.getEnvironmentDependentValue("email"), policyId);
        return this;
    }

    @Step
    public LoginStepsPatient waitInMin(int min) {
        long milSec = min * 60000;
        waitABit(milSec);
        return this;
    }
}
