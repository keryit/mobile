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

import java.time.Duration;

public class MainScreenPatientIOS implements MainScreenPatient {

    private AppiumDriver driver;

    @FindBy(xpath = "//*[@text='hamburgerPrimary24']")
    private MobileElement menuBtn;

    @FindBy(xpath = "//*[@text='Sign out' or @text='יציאה']")
    private MobileElement logoutBtn;

    @FindBy(xpath = "(//*[@class='UIATable']/*[./*[@class='UIAView'] and ./*[@class='UIAStaticText']])[3]")
    private MobileElement settingsBtn;

    @FindBy(id = "chevronRightHardGray24")
    private MobileElement languageBtn;

    @FindBy(xpath = "//*[@class='UIAView' and @height>0 and ./*[@class='UIAView' and ./*[@class='UIAView']] and ./parent::*[@class='UIATable'] and ./*[@class='UIAStaticText'] and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView']]")
    private MobileElement appointmentsBtnFromMenu;

    @FindBy(xpath = "//*[@class='UIAView' and @width>0 and @height>0 and ./parent::*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[./*[@class='UIAView']]] and ./*[@class='UIAStaticText']]")
    private MobileElement sighOutPopUp;

    @FindBy(xpath = "(//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./parent::*[@class='UIAView']]]]/*/*[@class='UIAButton' and ./parent::*[@class='UIAView']])[2]")
    private MobileElement signOutBtnOnPopUp;

    @FindBy(xpath = "(//*[@class='UIAView' and ./parent::*[@class='UIAView' and ./parent::*[@class='UIAView' and (./preceding-sibling::* | ./following-sibling::*)[@class='UIAView' and ./*[@class='UIAView']] and ./parent::*[@class='UIAView']]]]/*/*[@class='UIAButton' and ./parent::*[@class='UIAView']])[1]")
    private MobileElement cancelBtnOnSignOutPopUp;

    public MainScreenPatientIOS(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public void openMenu() {
        menuBtn.click();
    }

    @Override
    public void doSignOut() {
        openMenu();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(logoutBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Menu was not loaded");
        }
        logoutBtn.click();
        waitForSignOutPopUp();
        signOutBtnOnPopUp.click();
    }

    @Override
    public void openSettings() {
        openMenu();
        settingsBtn.click();
    }

    @Override
    public void changeLanguage(Locales locales) {
        openSettings();
        languageBtn.click();

        if (locales == Locales.en){
            driver.findElement(By.xpath("//*[@text='English']")).click();
        }
        if (locales == Locales.he){
            driver.findElement(By.xpath("//*[@text='עִברִית']")).click();
        }
        openMenu();
        appointmentsBtnFromMenu.click();

    }

    @Override
    public void openAppointmentsFromMenu() {
        openMenu();
        appointmentsBtnFromMenu.click();
    }

    @Override
    public void waitForSignOutPopUp() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {

            wait.until(ExpectedConditions.visibilityOf(signOutBtnOnPopUp));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Sign out pop-up was not loaded");
        }
    }
}
