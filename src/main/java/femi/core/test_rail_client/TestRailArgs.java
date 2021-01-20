package femi.core.test_rail_client;

import femi.core.utils.properties.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestRailArgs {

    //if the listener is enabled or not
    private Boolean enabled;
    //test plan id (if one already exists)
    private String testPlanId;
    //suite names
    private List<String> suiteNames;
    //url to the TestRail instance
    private String url;
    //username to login to TestRail
    private String username;
    //password to login to TestRail
    private String password;

    private String tmpSuites;


    public static TestRailArgs getNewTestRailListenerArgs() {
        TestRailArgs args = new TestRailArgs();
        args.initializeConnectionParams();
        args.initializeTestParams();

        args.enabled = Boolean.valueOf(System.getProperty("testrail.reporting.enabled")) == null?false: Boolean.valueOf(System.getProperty("testrail.reporting.enabled"));
       //TODO Uncomment for debug
//        args.enabled = true;
        if (!args.enabled) {
            return args;
        }

        String planId = args.getTestPlanId();
        if (planId == null) {
            throw new IllegalArgumentException("TestRail Test Plan ID not specified");
        } else {
            try {
                args.testPlanId = planId;
            } catch(NumberFormatException ex) {
                throw new IllegalArgumentException("Plan Id is not an integer as expected");
            }
        }

        String suiteNamesStr = System.getProperty("testRail.suite.names");
        //TODO uncomment for debug
//        String suiteNamesStr = args.tmpSuites;
        if (suiteNamesStr != null) {
            try {
                String[] suiteNamesArr = suiteNamesStr.split(",");
                args.suiteNames = new ArrayList<>();
                for (String suiteName : suiteNamesArr) {
                    if (suiteName != null && !suiteName.trim().isEmpty()) {
                        args.suiteNames.add(suiteName.trim());
                    }
                }

            } catch(NumberFormatException ex) {
                throw new IllegalArgumentException("Plan Id is not an integer as expected");
            }
        }

        if (args.url == null) {
            throw new IllegalArgumentException("TestRail URL not specified (testRail.url)");
        }

        if (args.username == null) {
            throw new IllegalArgumentException("TestRail user not specified (testRail.username)");
        }

        if (args.password == null) {
            throw new IllegalArgumentException("TestRail password not specified (testRail.password)");
        }

        return args;
    }

    private void initializeTestParams(){
        try {
            PropertyUtils connectionProps = new PropertyUtils("test_rail_suite.properties");
            Properties suiteProper = connectionProps.getPropValues();
            this.testPlanId = suiteProper.getProperty("testRail.testPlanId");

            //TODO use it for debug
            //this.tmpSuites = suiteProper.getProperty("testRail.suiteNames");
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void initializeConnectionParams(){
        try {
            PropertyUtils connectionProps = new PropertyUtils("test_rail_connection.properties");
            Properties conProp = connectionProps.getPropValues();
            this.url = conProp.getProperty("url");
            this.username = conProp.getProperty("user_name");
            this.password = conProp.getProperty("pass");

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getTestPlanId() {
        return testPlanId;
    }

    public List<String> getSuiteNames() {
        return suiteNames;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
