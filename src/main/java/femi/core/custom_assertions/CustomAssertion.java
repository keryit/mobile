package femi.core.custom_assertions;

import femi.core.utils.report.AllureHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class CustomAssertion extends Assert {

    public static void assetTextEquals(String textForVerifying, String expectedRegexFormatOrValue){
        if(null == textForVerifying && null == expectedRegexFormatOrValue){
            return;
        }
        if(null == expectedRegexFormatOrValue && textForVerifying != null){
            fail("expected a null value, but not null found. " + textForVerifying);
        }
        if(null != expectedRegexFormatOrValue && textForVerifying == null){
            fail("expected not null value, but null found. " + textForVerifying);
        }
        if(("{}".equals(expectedRegexFormatOrValue) && textForVerifying.equals(expectedRegexFormatOrValue))){
            return;
        }
        if(textForVerifying.matches(expectedRegexFormatOrValue)){
            return;
        }else{
            fail("expected format: [" + format(expectedRegexFormatOrValue) + "] " +
                    ", but was found value: [" + textForVerifying + "]");
        }
    }

    private static String format(String str){
        String tmp = str.replaceAll("[/\\\\s]","")
                .replaceAll("[/\\\\+]", "")
                .replaceAll("[/\\\\d]","digits");
        return tmp;
    }

    public static void fail(WebDriver driver, String message){
        AllureHelper.saveAllureScreenShot(message, driver);
        fail(message);
    }

    public static void assertFalse(WebDriver driver, boolean condition, String message){
        if(condition) {
            AllureHelper.saveAllureScreenShot(message, driver);
            fail(message);
        }
    }

    public static void assertTrue(WebDriver driver, boolean condition, String message){
        if(!condition) {
            AllureHelper.saveAllureScreenShot(message, driver);
            fail(message);
        }
    }
}
