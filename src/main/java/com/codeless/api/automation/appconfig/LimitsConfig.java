package com.codeless.api.automation.appconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitsConfig {

  private Map<String, Limit> limitByName;

  @Data
  public static class Limit {

    private Map<String, LimitData> limitByPlan;
  }

  @Data
  public static class LimitData {

    private Integer defaultLimit;
    private Map<String, Integer> override;
  }
}
