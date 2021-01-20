package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import femi.core.utils.Locales;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectOrganizationPatientAndroid implements SelectOrganizationPatient {

    private AppiumDriver driver;

    @FindBy(id = "organization_spinner")
    private MobileElement organizationField;

    @FindBy(id = "submit")
    private MobileElement btnNext;

    @FindBy(id = "language_layout")
    private MobileElement languageSelector;

    public SelectOrganizationPatientAndroid(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public void selectOrganization(String organizationName) {
        organizationField.click();
        driver.findElement(By.xpath("//*[@text='" + organizationName + "']")).click();
    }

    @Override
    public boolean isOrganizationScreenOpened() {

        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(btnNext));

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
            driver.findElement(By.id("radio_en")).click();
        }
        else if (locales == Locales.he){
            driver.findElement(By.id("radio_he")).click();
        }
        driver.findElement(By.id("ok_button")).click();
    }

}
