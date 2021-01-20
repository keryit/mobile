package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import femi.core.utils.Locales;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainScreenPatientAndroid extends PageObject implements MainScreenPatient {

    private AppiumDriver driver;

    @FindBy(className = "android.widget.ImageButton")
    private MobileElement menuBtn;

    @FindBy(id = "logout_button")
    private MobileElement logoutBtn;

    @FindBy(xpath = "(//*[@id='design_navigation_view']/*/*[@id='design_menu_item_text'])[2]")
    private MobileElement settingsBtn;

    @FindBy(xpath = "//*[@id='title' and (./preceding-sibling::* | ./following-sibling::*)[@text]]")
    private MobileElement languageBtn;

    @FindBy(xpath = "//*[@class='android.widget.ImageButton']")
    private MobileElement languageBackAfterSelecting;

    @FindBy(xpath = "(//*[@id='design_navigation_view']/*/*[@id='design_menu_item_text'])[1]")
    private MobileElement appointmentsBtnFromMenu;

    @FindBy(xpath = "//*[@class='android.widget.LinearLayout' and ./*[@id='header']]")
    private MobileElement sighOutPopUp;

    @FindBy(id = "ok_btn")
    private MobileElement signOutBtnOnPopUp;

    @FindBy(id = "cancel_button")
    private MobileElement cancelBtnOnSignOutPopUp;


    public MainScreenPatientAndroid(AppiumDriver appiumDriver) {
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
        languageBackAfterSelecting.click();
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

            wait.until(ExpectedConditions.visibilityOf(sighOutPopUp));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Sign out pop-up was not loaded");
        }
    }
}
