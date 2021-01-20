package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.custom_assertions.CustomAssertion;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppointmentsScreenPatientIOS implements AppointmentsScreenPatient {

    private AppiumDriver driver;

    @FindBy(className = "UIATable")
    private MobileElement appointmentsList;

    public AppointmentsScreenPatientIOS(AppiumDriver appiumDriver) {
        this.driver = appiumDriver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }


    @Override
    public void waitForAppointmentListIsLoaded() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        try {

            wait.until(ExpectedConditions.visibilityOf(appointmentsList));
//            wait.until(ExpectedConditions.visibilityOf(missedBtn));
//            wait.until(ExpectedConditions.visibilityOf(completedBtn));

        } catch (Exception e) {
            CustomAssertion.fail(driver, "Appointment list was not loaded or login failed");
        }
    }

    @Override
    public void openAppointment() {

    }
}
