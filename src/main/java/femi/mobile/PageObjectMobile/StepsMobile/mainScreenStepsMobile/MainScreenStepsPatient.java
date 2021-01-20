package femi.mobile.PageObjectMobile.StepsMobile.mainScreenStepsMobile;

import femi.core.utils.report.AllureHelper;
import femi.mobile.PageObjectMobile.PatientPortalApp.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import ru.yandex.qatools.allure.annotations.Step;

public class MainScreenStepsPatient {

    private AppointmentsScreenPatient appointmentsScreen;
    private MainScreenPatient mainScreenPatient;
    private AppiumDriver driver;

    public MainScreenStepsPatient(AppiumDriver driver) {
            this.driver = driver;
        if (driver instanceof AndroidDriver) {
            appointmentsScreen = new AppointmentsScreenPatientAndroid(driver);
            mainScreenPatient = new MainScreenPatientAndroid(driver);
        } else if (driver instanceof IOSDriver) {
            appointmentsScreen = new AppointmentsScreenPatientIOS(driver);
            mainScreenPatient = new MainScreenPatientIOS(driver);
        }
    }

    // ==================================================================== APPOINTMENTS =============================================================================================

    @Step
    public MainScreenStepsPatient isAppointmentsScreenOpenedPatientPortal(){
        appointmentsScreen.waitForAppointmentListIsLoaded();
        AllureHelper.saveAllureScreenShot("Appointments screen", driver);
        return this;
    }

    @Step
    public void doSignOutPatientPortal(){
        mainScreenPatient.doSignOut();
    }
}
