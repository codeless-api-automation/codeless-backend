package com.codeless.api.automation.appconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryConfig {

  private Map<String, Country> countryByIso2Name;

  @Data
  public static class Country {
    private String displayName;
    private Map<String, RegionDetails> regionByName;
  }

  @Data
  public static class RegionDetails {
    // populated on read
    private String countryDisplayName;
    private String countryIso2Name;

    private String name;
    private String awsCloudRegion;
    private boolean defaultRegion;
  }
}
