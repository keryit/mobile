package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppointmentsScreenPatientAndroid implements AppointmentsScreenPatient {

    private AppiumDriver driver;

    @FindBy(id = "recycler_view")
    private MobileElement appointmentsList;

    @FindBy(id = "completed_filter_button")
    private MobileElement completedBtn;

    @FindBy(id = "missed_filter_button")
    private MobileElement missedBtn;

    @FindBy(id = "scheduled_filter_button")
    private MobileElement scheduledBtn;

    public AppointmentsScreenPatientAndroid(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public void waitForAppointmentListIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        try {

            wait.until(ExpectedConditions.visibilityOf(appointmentsList));
            wait.until(ExpectedConditions.visibilityOf(missedBtn));
            wait.until(ExpectedConditions.visibilityOf(completedBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Appointment list was not loaded o login failed");
        }
    }

    @Override
    public void openAppointment() {

    }
}
