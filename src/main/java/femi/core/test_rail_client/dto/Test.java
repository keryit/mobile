package femi.core.test_rail_client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test {
    public int id;
    @JsonProperty("case_id")
    public int caseId;
    @JsonProperty("statusId")
    public int status_id;
    @JsonProperty("run_id")
    public int runId;
    //following property is not present in TestRail by default
    //we need to add it as a custom field
    @JsonProperty("custom_automation_id")
    public String automationId;
    @JsonProperty("title")
    public String title;

}
