package femi.core.utils;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.steps.ScenarioSteps;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static femi.core.utils.UtilityFunctions.convertObjToDouble;

public class DriverUtils extends ScenarioSteps{

    public static void waitAndAcceptAlert(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }

    public static WebDriverWait wait(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        return wait;
    }

    public static void scrollToWebElement(WebDriver driver, WebElement webElement){
        // Create instance of Javascript executor
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript("arguments[0].scrollIntoView(true);",webElement);
    }

    public static void scrollDown(WebDriver driver, WebElement elementForScrolling){
        // Create instance of Javascript executor
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript("arguments[0].scrollIntoView(false);", elementForScrolling);
    }

    public static Double getBoundingClientRect(WebDriver driver, WebElement webElement){
        Double result = 0.0;
        JavascriptExecutor je = (JavascriptExecutor) driver;
        try{
            result = convertObjToDouble(je.executeScript("return Math.round(arguments[0].getBoundingClientRect().top * 1000) / 1000",
                    webElement));
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void refreshPage(WebDriver webDriver){
        webDriver.navigate().refresh();
    }

    public void waitForTextPresentInTheElement(WebElementFacade webElement){
        String text = "";
        int timesToTryToCheckIsTextLoaded = 10;
        waitABit(500);
        while(timesToTryToCheckIsTextLoaded > 0 && text.isEmpty()){
            if(webElement.isCurrentlyVisible()) {
                text = webElement.getTextValue();
                if (!text.isEmpty()) { break; }
            }
            timesToTryToCheckIsTextLoaded--;
            waitABit(500);
        }
    }

    public void waitForTextPresentInTheElement(WebDriver driver, WebElementFacade webElement){
        waitABit(1000);
        WebDriverWait wait = new WebDriverWait(driver,120);
        wait.until(CustomExpectedConditions.elementIsNotEmpty(webElement));
    }
}
