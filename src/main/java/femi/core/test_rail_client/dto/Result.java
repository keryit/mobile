package femi.core.test_rail_client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
    public int id;
    @JsonProperty("test_id")
    public int testId;
    @JsonProperty("status_id")
    public Integer statusId;
    public String comment;
}
