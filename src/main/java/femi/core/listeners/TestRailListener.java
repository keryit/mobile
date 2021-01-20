package femi.core.listeners;

import femi.core.test_rail_client.ResultStatus;
import femi.core.test_rail_client.TestRailReporter;
import femi.core.test_rail_client.annotation.TestRailCase;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;


public class TestRailListener implements ITestListener, IConfigurationListener {

    private Logger LOG = Logger.getLogger(TestRailListener.class.getName());

    private TestRailReporter reporter;
    private boolean enabled;

    public static String testUri;
    public static String testBrowser;
    public static String appType;

    /**
     * Store the result associated with a failed configuration here. This can
     * then be used when reporting the result of a skipped test to provide
     * additional information in TestRail
     */
    private ThreadLocal<ITestResult> testSkipResult = new ThreadLocal<ITestResult>();

    public TestRailListener() {
        try {
            reporter = TestRailReporter.getInstance();
            enabled = reporter.isEnabled();
        } catch (Throwable ex) {
            LOG.severe("Ran into exception initializing reporter: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Reports the result for the test method to TestRail
     * @param result TestNG test result
     */
    private void reportResult(ITestResult result) {
        if (!enabled) {
            return;
        }
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            String className = result.getTestClass().getName();
            String methodName = result.getMethod().getMethodName();
            String id = className + "#" + methodName;
            Object[] params = result.getParameters();
            String firstParam = null;
            if (params != null && params.length > 0) {
                id += "(" + params[0] + ")";
                firstParam = String.valueOf(params[0]);
            }
            int status = result.getStatus();
            Throwable throwable = result.getThrowable();

            TestRailCase trCase = method.getAnnotation(TestRailCase.class);
            Test test = method.getAnnotation(Test.class);
            String tcName;
            String [] tcNames = null;
            if (trCase == null) {
                if (!test.dataProvider().isEmpty()) {
                    if (firstParam == null) {
                        LOG.severe("Didn't find the first parameter for DD test " + id + ". Result not reported.");
                        return;
                    }
                    tcName = firstParam;
                } else {
                    LOG.severe(String.format("Test case %s is not annotated with TestRailCase annotation. " +
                            "Result not reported", id));
                    return;
                }
            } else {
                tcName = trCase.value();
            }

            if (tcName == null || tcName.isEmpty()) {
                if(trCase.testCaseNames().length > 0){
                    tcNames = trCase.testCaseNames();
                }
                //case id not specified on method, check if this is a DD method
                else if (!trCase.selfReporting()) {
                    //self reporting test cases are responsible of reporting results on their own
                    LOG.warning("Didn't find automation id nor is the test self reporting for test " + id +
                            ". Please check test configuration.");
                    return;
                } else {
                    return;
                }
            }

            Map<String, Object> props = new HashMap<>();
            long elapsed = (result.getEndMillis() - result.getStartMillis()) / 1000;
            elapsed = elapsed == 0 ? 1 : elapsed; //we can only track 1 second as the smallest unit
            props.put("elapsed",  elapsed + "s");
            props.put("status", getStatus(status));
            props.put("throwable", throwable);
            if (status == ITestResult.SKIP) {
                ITestResult skipResult = testSkipResult.get();
                if (skipResult != null) {
                    props.put("throwable", skipResult.getThrowable());
                }
            }
            props.put("screenshotUrl", getScreenshotUrl(result));
            Map<String, String> moreInfo = new LinkedHashMap<>();
            moreInfo.put("class", result.getMethod().getRealClass().getCanonicalName());
            moreInfo.put("method", result.getMethod().getMethodName());
            if (result.getParameters() != null) {
                moreInfo.put("parameters", "[ApplicationType=" + appType +  ", Browser=" + testBrowser + ",  TestUrl=" + testUri + "]");
            }
            moreInfo.putAll(getMoreInformation(result));
            props.put("moreInfo", moreInfo);
            if(tcNames != null && tcNames.length > 0){
                reporter.reportResult(tcNames, props);
            }else {
                reporter.reportResult(tcName, props);
            }
        } catch(Exception ex) {
               LOG.severe("Ran into exception " + ex.getMessage());
        }
    }

    public void onTestStart(ITestResult result) {
        //not reporting a started status
    }

    public void onTestSuccess(ITestResult result) {
        reportResult(result);
    }

    public void onTestFailure(ITestResult result) {
        reportResult(result);
    }

    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() != null) {
            //test failed, but is reported as skipped because of RetryAnalyzer.
            //so, changing result status and reporting this as failure instead.
            result.setStatus(ITestResult.FAILURE);
        }
        reportResult(result);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //nothing here
    }

    public void onStart(ITestContext context) {
        //nothing here
    }

    public void onFinish(ITestContext context) {
        //nothing here
    }

    /**
     * TestRail currently doesn't support uploading screenshots via APIs. Suggested method is
     * to upload screenshots to another server and provide a URL in the test comments.
     *
     * This method should be overridden in a sub-class to provide the URL for the screenshot.
     *
     * @param result result of test execution
     * @return the URL to where the screenshot can be accessed
     */
    public String getScreenshotUrl(ITestResult result) {
        return "attachments/screenshot.png";
    }

    /**
     * In case, we want to log more information about the test execution, this method can be used.
     *
     * NOTE: the test class/method/parameter information is automatically logged.
     *
     * This method should be overridden in a sub-class to provide map containing information
     * that should be displayed for each test result in TestRail
     */
    public Map<String, String> getMoreInformation(ITestResult result) {
        return Collections.emptyMap();
    }

    /**
     * @param status TestNG specific status code
     * @return TestRail specific status IDs
     */
    private ResultStatus getStatus(int status) {
        switch (status) {
            case ITestResult.SUCCESS:
                return ResultStatus.PASS;
            case ITestResult.FAILURE:
                return ResultStatus.FAIL;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                return ResultStatus.FAIL;
            case ITestResult.SKIP:
                return ResultStatus.SKIP;
            default:
                return ResultStatus.UNTESTED;
        }
    }

    public void onConfigurationSuccess(ITestResult iTestResult) {
        testSkipResult.remove();
    }

    public void onConfigurationFailure(ITestResult iTestResult) {
        testSkipResult.set(iTestResult);
    }

    public void onConfigurationSkip(ITestResult iTestResult) {
        //nothing here
    }
}
