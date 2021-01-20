package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OTPScreenPatientIOS implements OTPScreenPatient {

    private AppiumDriver driver;

    @FindBy(className = "UIATextField")
    private MobileElement otpInputField;

    @FindBy(xpath = "//*[@class='UIAButton' and ./parent::*[@class='UIAView']]")
    private MobileElement verifyBtn;

    @FindBy(xpath = "//*[@text='Send code again' or @text='שלח קוד שוב']")
    private MobileElement sendCodeAgainLink;

    @FindBy(xpath = "//*[@class='UIAButton' and ./parent::*[@text]]")
    private MobileElement backBtn;

    public OTPScreenPatientIOS(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public boolean isOTPOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(verifyBtn));
            wait.until(ExpectedConditions.visibilityOf(sendCodeAgainLink));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "OTP screen was not loaded or Verify button locator not found.");
        }
        return true;
    }

    @Override
    public void enterOTP(String otpCode) {
        otpInputField.click();
        otpInputField.clear();
        otpInputField.sendKeys(otpCode);
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

    public void clickBackBtn() {
        backBtn.click();
    }
}
