package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginScreenPatientAndroid implements LoginScreenPatient {

    private AppiumDriver driver;

    @FindBy(id = "field_icon")
    private MobileElement userIcon;

    @FindBy(id = "edit_text")
    private MobileElement policyIdInputField;

    @FindBy(xpath = "//*[@id='edit_text' and ./parent::*[./parent::*[./parent::*[@id='phone_edit']]]]")
    private MobileElement phoneNumberField;

    @FindBy(id = "submit")
    private MobileElement nextBtn;

    @FindBy(id = "logo")
    private MobileElement logo;

    @FindBy(id = "terms_tv")
    private MobileElement termsAndConditiondsForm;

    @FindBy(id = "decline_btn")
    private MobileElement declineTermsAndConditionsBtn;

    @FindBy(id = "accept_btn")
    private MobileElement agreeTermsAndConditionsBtn;

    @FindBy(id = "ok_btn")
    private MobileElement declineTermsAndConditionsPopUp;

    @FindBy(id = "cancel_btn")
    private MobileElement cancelBtnOnDeclinePopUp;


    public LoginScreenPatientAndroid(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public boolean isLoginScreenOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(logo));
            wait.until(ExpectedConditions.visibilityOf(userIcon));

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

    @Override
    public boolean isLogoPresent() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(logo));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Logo is not present on Login Screen");
        }
        return true;
    }

    @Override
    public boolean isTermsAndConditionsFormAppears() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(termsAndConditiondsForm));

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
        declineTermsAndConditionsPopUp.click();
    }

    @Override
    public void cancelDeclineTermsAndConditionsOnPopUp() {
        cancelBtnOnDeclinePopUp.click();
    }
}
