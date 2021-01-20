package femi.mobile.mobileUtils;

import femi.mobile.App;
import femi.mobile.OS;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;

import java.net.MalformedURLException;

public interface AppiumController {


    AppiumDriver setUpApp(OS executionOS, App appType, String URI, String port, String UDID) throws MalformedURLException;

    void removeApp(AppiumDriver driver, App appType);

    void collectTCResult(ITestResult result) throws Exception;
}
