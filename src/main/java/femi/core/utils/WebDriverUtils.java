package femi.core.utils;

import femi.core.custom_assertions.CustomAssertion;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebDriverUtils {
    static Logger LOG = LoggerFactory.getLogger(WebDriverUtils.class);

    public static void waitForElementPresence(WebDriver driver, WebElement webElement){
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            wait(driver, 7);
            wait.until(ExpectedConditions.visibilityOf(webElement));
        }catch(Exception ex){
            CustomAssertion.fail(driver,"Element was not found." + webElement);
        }
    }

    public static void waitForPageLoadByURIPart(WebDriver driver, String uriPart){
        WebDriverWait wait = new WebDriverWait(driver,60);
        wait.until(ExpectedConditions.urlContains(uriPart));
    }

    public static void wait(WebDriver driver, int seconds){
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public static WebDriver activateVideoAndAudioInChromeBrowser(WebDriver driver){
        ChromeDriverService driverService = ChromeDriverService.createDefaultService();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("use-fake-device-for-media-stream","use-fake-ui-for-media-stream");
        driver = new ChromeDriver(driverService,chromeOptions);
        return driver;
    }

    public static List<String> getMissedElementsOnThePage(HashMap<String,By> expectedElementsOnPage, WebDriver driver){
        ArrayList<String> missedElements = new ArrayList<>();
        for(String element : expectedElementsOnPage.keySet()){
            WebElement el;
            try {
                el = driver.findElement(expectedElementsOnPage.get(element));
                waitForElementPresence(driver, el);
            }catch(Exception ex){
                LOG.info(ex.getMessage());
                continue;
            }
            if(el == null || !el.isEnabled()){
                missedElements.add(element);
            }
        }
        return missedElements;
    }

    public static ChromeOptions getChromeOptionsWithActivatedVideoAndAudio() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-file-access-from-files");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--disable-notifications");
        return options;
    }

    public static void pressKey(WebDriver driver, Keys...keys) {
        Actions action = new Actions(driver);
        for(Keys key : keys){
            action.sendKeys(key);
        }
        action.build();
        Action selectMultiple = action.build();
        selectMultiple.perform();

    }
}

