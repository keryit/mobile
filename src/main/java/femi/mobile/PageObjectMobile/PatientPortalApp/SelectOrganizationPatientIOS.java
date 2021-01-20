package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import femi.core.utils.Locales;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectOrganizationPatientIOS implements SelectOrganizationPatient {

    private AppiumDriver driver;

    @FindBy(className = "UIATextField")
    private MobileElement organizationField;

    @FindBy(className = "UIAImage")
    private MobileElement logo;

    @FindBy(className = "UIAButton")
    private MobileElement btnNext;

    @FindBy(xpath = "((//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView']]]]/*/*[@class='UIAView' and ./parent::*[@class='UIAView']])[2]/*[@class='UIAButton'])[2]")
    private MobileElement languageSelector;

    public SelectOrganizationPatientIOS(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }


    @Override
    public void selectOrganization(String organizationName) {
        organizationField.click();
        driver.findElement(MobileBy.className("XCUIElementTypePickerWheel")).sendKeys(organizationName);
    }

    @Override
    public boolean isOrganizationScreenOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(languageSelector));
            wait.until(ExpectedConditions.visibilityOf(organizationField));
            wait.until(ExpectedConditions.visibilityOf(logo));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Select Organization screen was not loaded or select organization drop down locator not found.");
        }
        return true;
    }

    @Override
    public void clickNextButton() {
        btnNext.click();
    }

    @Override
    public void selectLanguage(Locales locales) {
        languageSelector.click();
        if (locales == Locales.en){
            driver.findElement(By.id("English")).click();
        }
        else if (locales == Locales.he){
            driver.findElement(By.id("עברית")).click();
        }
    }
}
