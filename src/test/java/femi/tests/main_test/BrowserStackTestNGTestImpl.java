package femi.tests.main_test;

import com.browserstack.local.Local;
import femi.core.utils.WebDriverUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static femi.core.utils.report.AllureHelper.saveAllureScreenShot;
import static femi.tests.main_test.BaseTest.isCloseBrowserOnFailure;


public class BrowserStackTestNGTestImpl implements BaseTestNGTest {
    public RemoteWebDriver driver;
    private Map<RemoteWebDriver, Local> webDriverInstances = new HashMap<>();
    static private Logger LOGGER = LoggerFactory.getLogger(BrowserStackTestNGTestImpl.class);
    private String config_file;
    private String environment;
    private String username;
    private String accessKey;
    private JSONObject config;
    private String failReason;

    static Logger LOG = LoggerFactory.getLogger(BrowserStackTestNGTestImpl.class);

    @BeforeClass
    @org.testng.annotations.Parameters(value={"browser", "config", "environment"})
    public void start(String browser, String config_file, String environment) throws Exception {
        this.config_file = config_file;
        this.environment = environment;
        this.failReason = "";
    }

    @Override
    public RemoteWebDriver getNewWebDriverInstance(String URI, String testStepName){
        RemoteWebDriver webDriver = getBrowserInstance(testStepName);
        webDriver.get(URI);
        return webDriver;
    }

    @Override
    public void openStartPage(String URI) {
        RemoteWebDriver webDriver = getBrowserInstance("Open start page.");
        webDriver.get(URI);
    }

    @Override
    public void openStartPageInTheSameWindow(String URI, WebDriver webDriver) {
        webDriver.get(URI);
    }

    @AfterClass
    public void tearDown(ITestContext testContext) throws Exception {
        for(RemoteWebDriver driverCur : webDriverInstances.keySet()) {
            Local l = webDriverInstances.get(driverCur);
            URI uri = new URI("https://" + username +":"+ accessKey + "@api.browserstack.com/automate/sessions/" + driverCur.getSessionId() + ".json");
            HttpPut putRequest = new HttpPut(uri);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            String result = (testContext.getFailedTests().size() > 0) && !failReason.isEmpty()? "error":"completed";
            nameValuePairs.add((new BasicNameValuePair("status", result)));
            nameValuePairs.add((new BasicNameValuePair("reason",  failReason )));

            putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpClientBuilder.create().build().execute(putRequest);
            driverCur.quit();
            if (l != null) l.stop();
        }
    }

    @AfterMethod
    public void collectTCResult(ITestResult result) throws Exception {
        if(result.getThrowable() != null) {
            failReason = "FailedTestCaseName=" + result.getName() + "; Reason: '" + result.getThrowable().getMessage() + "'";
            LOG.info("Failed: " + failReason);
            if(!failReason.matches("(?i).*email.*") && isCloseBrowserOnFailure)
              tearDown(result.getTestContext());
        }
    }

    @Override
    public void closeBrowserInstance(WebDriver webDriver) {
        webDriverInstances.remove(webDriver);
        if(webDriver != null)
            webDriver.close();
    }

    @Override
    public void takeScreenshotsForAllBrowserInstances(String tcName) {
        for(RemoteWebDriver driverCur : webDriverInstances.keySet()) {
            if(driverCur != null)
             saveAllureScreenShot("Title: " + driverCur.getTitle() + " " + System.currentTimeMillis(), driverCur);
        }
    }

    private RemoteWebDriver getBrowserInstance(String testStepName){
        Local l = null;
        JSONParser parser = new JSONParser();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        JSONObject envs;
        RemoteWebDriver driver = null;

        try {
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/config/" + config_file));
            envs = (JSONObject) config.get("environments");
            Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
            Iterator it = envCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }

            Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
            it = commonCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (capabilities.getCapability(pair.getKey().toString()) == null) {
                    capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
                }
            }

            username = System.getenv("BROWSERSTACK_USERNAME");
            if (username == null) {
                username = (String) config.get("user");
            }

            accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
            if (accessKey == null) {
                accessKey = (String) config.get("key");
            }

            if (capabilities.getCapability("browserstack.local") != null && capabilities.getCapability("browserstack.local") == "true") {
                l = new Local();
                Map<String, String> options = new HashMap<>();
                options.put("key", accessKey);
                l.start(options);
            }

            if(testStepName != null && !testStepName.isEmpty())capabilities.setCapability("name", testStepName);
            capabilities.setCapability(ChromeOptions.CAPABILITY, WebDriverUtils.getChromeOptionsWithActivatedVideoAndAudio());

            driver = new RemoteWebDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
            webDriverInstances.put(driver, l);
        }catch(Exception e){
            e.printStackTrace();
        }
        return driver;
    }
}
