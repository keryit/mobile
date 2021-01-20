package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OTPScreenPatientAndroid implements OTPScreenPatient {

    private AppiumDriver driver;

    @FindBy(id = "edit_text")
    private MobileElement otpInputField;

    @FindBy(id = "submit")
    private MobileElement verifyBtn;

    @FindBy(id = "send_sms_again")
    private MobileElement sendCodeAgainLink;

    public OTPScreenPatientAndroid(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public boolean isOTPOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(verifyBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "OTP screen was not loaded or Verify button locator not found.");
        }
        return true;
    }

    @Override
    public void enterOTP(String otpCode) {
        otpInputField.click();
        otpInputField.clear();
        otpInputField.setValue(otpCode);
    }

    @Override
    public MobileElement getOtp() {
        return otpInputField;
    }

    @Override
    public void clickVerifyMeOnOtp() {
        verifyBtn.click();
    }

    @Override
    public void clickSendCodeAgain() {
        sendCodeAgainLink.click();
    }
}
