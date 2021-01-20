package femi.core.utils.report;


import femi.core.utils.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.util.HashMap;

public class AllureHelper {
    @Attachment(value = "{0}",type = "text/json")
    public static String logRequestResponseAsJSON(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{0}",type = "text/xml")
    public static String logRequestResponseAsXML(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{0}",type = "text/html")
    public static String logResponseAsHTML(String attachName, String message) {
        return message;
    }

    @Attachment(value = "{0}",type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Attachment(value = "{0}",type = "image/png")
    public static byte[] saveAllureScreenShot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}",type = "image/png")
    public static byte[] saveAllureScreenShot(String attachName, WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void setEnvironmentData(String endPoint, String dbUri){
        createEnvFile("src/test/resources/environments/environment_pattern.xml", endPoint, dbUri, "patient");
    }

    private static void createEnvFile(String path,String endPoint, String dbUri, String type){
        String fileContent = FileUtils.readFileByAbsolutePath(path);
        String content = FileUtils.replaceParametrizedData(new HashMap(){{
            put("EndPoint", endPoint);
        }},fileContent);
        FileUtils.writeFile("src/test/resources/environments/environment.xml", content);
    }
}
