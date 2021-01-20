package femi.core.test_rail_client;


import femi.core.test_rail_client.dto.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class TestRailReporter {

    private Logger LOG = Logger.getLogger(TestRailReporter.class.getName());
    private TestRailClient client;
    private Map<String, Integer> caseIdLookupMap;
    private Map<String, Integer> testToRunIdMap;

    private Boolean enabled;
    private String config;
    private List<String> suiteRunNames;

    //keys for the properties map that is used to pass test information into this reporter
    public static final String KEY_MORE_INFO = "moreInfo";
    public static final String KEY_SCREENSHOT_URL = "screenshotUrl";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ELAPSED = "elapsed";
    public static final String KEY_THROWABLE = "throwable";

    private static class Holder {
        private static final TestRailReporter INSTANCE = new TestRailReporter();
    }

    public static TestRailReporter getInstance() {
        return Holder.INSTANCE;
    }

    private TestRailReporter(){
        TestRailArgs args = TestRailArgs.getNewTestRailListenerArgs();
        enabled = args.getEnabled();
        enabled = enabled == null ? false : enabled;

        //TODO uncomment for debug
        //enabled = true;

        if (!enabled) {
            LOG.info("TestRail listener is not enabled. Results will not be reported to TestRail.");
            return;
        }
        LOG.info("TestRail listener is enabled. Configuring...");
        try {
             this.suiteRunNames = args.getSuiteNames();

             client = new TestRailClient(args.getUrl(), args.getUsername(), args.getPassword());

             Plan plan = client.getPlan(args.getTestPlanId());
             Set<Integer> suiteIdSet = new HashSet<>();
             List<PlanEntry> planEntries = plan.entries;

            int projectId = 0;
            int suiteId = 0;
            testToRunIdMap = new HashMap<>();
            for (PlanEntry entry : planEntries) {
                suiteIdSet.add(suiteId = entry.suiteId);
                for (Run run : entry.runs) {
                    /**Verifying that current run = run for which we want to update status*/
                    if(args.getSuiteNames().contains(run.name)) {
                        projectId = run.projectId;
                        List<Test> tests = client.getTests(run.id);
                        for (Test test : tests) {
                            testToRunIdMap.put(test.title, run.id);
                        }
                    }
                }
            }

            caseIdLookupMap = cacheCaseIdLookupMap(client, projectId, suiteId);

            if (suiteIdSet.size() != 1) {
                throw new IllegalStateException("Referenced plan " + plan.id + " has multiple test suites (" +
                        suiteIdSet + "). This configuration is currently not supported.");
            }
            config = System.getProperty("testRail.runConfig");
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Integer> cacheCaseIdLookupMap(TestRailClient client, int projectId, int suiteId)
            throws IOException, ClientException {
        List<Case> cases = client.getCases(projectId, suiteId, 0, null);
        Map<String, Integer> lookupMap = new HashMap<>();
        for (Case c : cases) {
            if (c.title == null || c.title.isEmpty()) {
                continue;
            }
            if (lookupMap.get(c.title) != null) {
                LOG.severe("Found multiple tests cases with same titles. " +
                        "Case Ids " + lookupMap.get(c.title) + " & " + c.id);
            } else {
                lookupMap.put(c.title, c.id);
            }
        }
        return lookupMap;
    }

    public void reportResult(String tcTitle, Map<String, Object> properties) {
        ResultStatus resultStatus = (ResultStatus)properties.get(KEY_STATUS);
        Throwable throwable = (Throwable)properties.get(KEY_THROWABLE);
        String elapsed = (String)properties.get(KEY_ELAPSED);
        String screenshotUrl = (String)properties.get(KEY_SCREENSHOT_URL);
        Map<String, String> moreInfo = (Map<String, String>)properties.get(KEY_MORE_INFO);

        try {
            Integer caseId = caseIdLookupMap.get(tcTitle);
            if (caseId == null) {
                LOG.severe("Didn't find case id for test with title " + tcTitle);
                return;
            }

            StringBuilder comment = new StringBuilder("Automation info:\n");
            if (moreInfo != null && !moreInfo.isEmpty()) {
                for (Map.Entry<String, String> entry: moreInfo.entrySet()) {
                    comment.append("- ").append(entry.getKey()).append(" : ")
                            .append('`').append(entry.getValue()).append("`\n");
                }
            } else {
                comment.append("- `none`\n");
            }
            comment.append("\n");
            if (screenshotUrl != null && !screenshotUrl.isEmpty()) {
                comment.append("![](").append(screenshotUrl).append(")\n\n");
            }
            if (resultStatus.equals(ResultStatus.SKIP)) {
                comment.append("Test skipped because of configuration method failure. " +
                        "Related config error (if captured): \n\n");
                comment.append(getStackTraceAsString(throwable));
            }
            if (resultStatus.equals(ResultStatus.FAIL)) {
                comment.append("Test failed with following exception (if captured): \n\n");
                comment.append(getStackTraceAsString(throwable));
            }

            //add the result
            Map<String, Object> body = new HashMap<>();
            body.put("status_id", getStatus(resultStatus));
            body.put("comment", new String(comment.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            body.put("elapsed", elapsed);

            Integer runId = testToRunIdMap.get(tcTitle);
            if (runId == null) {
                throw new IllegalArgumentException("Unable to find pair for test with summary: ['"
                        + tcTitle + "'] and suite(s): " + this.suiteRunNames);
            }
            client.addResultForCase(runId, caseId, body);
        } catch(Exception ex) {
            LOG.severe("Ran into exception " + ex.getMessage());
        }
    }

    public void reportResult(String [] tcTitles, Map<String, Object> properties) {
        try {
            for(String tcTitle : tcTitles) {
                reportResult(tcTitle, properties);
            }
        } catch(Exception ex) {
            LOG.severe("Ran into exception " + ex.getMessage());
        }
    }

    private String getStackTraceAsString(Throwable throwable) throws UnsupportedEncodingException {
        if (throwable == null) {
            return "";
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(os));
        String str = new String(os.toByteArray(), "UTF-8");
        str = "    " + str.replace("\n", "\n    ").replace("\t", "    "); //better printing
        return str;
    }

    private int getStatus(ResultStatus status) {
        switch (status) {
            case PASS:
                return 1; //Passed
            case FAIL:
                return 5; //Failed
            case SKIP:
                return 2; //Blocked
            default:
                return 3; //Untested
        }
    }

    public boolean isEnabled(){
        return this.enabled;
    }

}
