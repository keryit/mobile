package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginScreenPatientIOS implements LoginScreenPatient {

    private AppiumDriver driver;

    @FindBy(className = "UIATextField")
    private MobileElement policyIdInputField;

    @FindBy(xpath = "//*[@placeholder='מספר טלפון' or @placeholder='Phone Number']")
    private MobileElement phoneNumberField;

    @FindBy(xpath = "//*[@class='UIAButton' and ./parent::*[@class='UIAView']]")
    private MobileElement nextBtn;

    @FindBy(className = "UIAImage")
    private MobileElement logo;

    @FindBy(xpath = "//*[@class='UIAButton' and ./parent::*[@text]]")
    private MobileElement backBtn;

    @FindBy(xpath = "((//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAWindow']]]]]/*[@class='UIAView'])[4]/*[@class='UIAButton'])[1]")
    private MobileElement declineTermsAndConditionsBtn;

    @FindBy(xpath = "((//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAWindow']]]]]/*[@class='UIAView'])[4]/*[@class='UIAButton'])[2]")
    private MobileElement agreeTermsAndConditionsBtn;

    @FindBy(xpath = "//*[@class='UIAView' and @width>0 and @height>0 and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@class='UIAView']] and ./*[@class='UIAStaticText']]")
    private MobileElement declineTermsAndConditionsPopUp;

    @FindBy(xpath = "(//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./parent::*[@class='UIAView']]]]/*/*[@class='UIAButton'])[1]")
    private MobileElement cancelBtnOnDeclinePopUp;

    @FindBy(xpath = "(//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./parent::*[@class='UIAView']]]]/*/*[@class='UIAButton'])[2]")
    private MobileElement declineBtnOnDeclinePopUp;


    public LoginScreenPatientIOS(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public boolean isLoginScreenOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.visibilityOf(logo));
            wait.until(ExpectedConditions.visibilityOf(backBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Enter Policy Id screen was not loaded or user icon locator not found.");
        }
        return true;
    }

    @Override
    public boolean isLoginScreenWithPhoneNumberOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.visibilityOf(policyIdInputField));
            wait.until(ExpectedConditions.visibilityOf(phoneNumberField));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Enter Policy Id and Phone Number screen was not loaded or user icon locator not found.");
        }
        return true;
    }

    @Override
    public void putPatientPolicyId(String policyId) {
        policyIdInputField.click();
        policyIdInputField.clear();
        policyIdInputField.sendKeys(policyId);
    }

    @Override
    public MobileElement getPolicyIdField() {
        return policyIdInputField;
    }

    @Override
    public void putPatientPhone(String phoneNumber) {
        phoneNumberField.click();
        phoneNumberField.clear();
        phoneNumberField.sendKeys(phoneNumber);
    }

    @Override
    public MobileElement getPatientPhoneField() {
        return phoneNumberField;
    }

    @Override
    public void clickNextBtn() {
        nextBtn.click();
    }

    public void clickBackBtn() {
        backBtn.click();
    }

    @Override
    public boolean isLogoPresent() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(logo));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Logo is not present on the Login screen");
        }
        return true;
    }

    @Override
    public boolean isTermsAndConditionsFormAppears() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        try {

            wait.until(ExpectedConditions.visibilityOf(agreeTermsAndConditionsBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Terms and Conditions not appeared");
        }
        return true;
    }

    @Override
    public void confirmTermAndConditions() {
        agreeTermsAndConditionsBtn.click();
    }

    @Override
    public void declineTermsAndConditions() {
        declineTermsAndConditionsBtn.click();
    }

    @Override
    public boolean waitDeclineTermsAndConditionsPopUp() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(declineTermsAndConditionsPopUp));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "There is no decline pop-up");
        }
        return true;
    }

    @Override
    public void confirmDeclineTermsAndConditionOnPopUp() {
        declineBtnOnDeclinePopUp.click();
    }

    @Override
    public void cancelDeclineTermsAndConditionsOnPopUp() {
        cancelBtnOnDeclinePopUp.click();
    }
}
