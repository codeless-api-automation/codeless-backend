package com.codeless.api.automation.appconfig;

import com.codeless.api.automation.appconfig.CountryConfig.Country;
import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;

@RequiredArgsConstructor
public class CountryConfigProvider {

  private final ObjectMapper objectMapper;

  public CountryConfig getCountryConfig() {
    try {
      File file = ResourceUtils.getFile("classpath:config/countries.json");
      String countries = new String(Files.readAllBytes(file.toPath()));
      return objectMapper.readValue(countries, CountryConfig.class);
    } catch (Exception exp) {
      throw new RuntimeException(exp);
    }
  }

  public Map<String, RegionDetails> getRegions() {
    Map<String, RegionDetails> regionByName = new HashMap<>();
    CountryConfig countryConfig = getCountryConfig();
    Map<String, Country> countryByIso2 = countryConfig.getCountryByIso2Name();
    for (Map.Entry<String, Country> countryEntry : countryByIso2.entrySet()) {
      for (Map.Entry<String, RegionDetails> regionDetailsEntry
          : countryEntry.getValue().getRegionByName().entrySet()) {
        RegionDetails regionDetail = regionDetailsEntry.getValue();
        regionDetail.setCountryIso2Name(countryEntry.getKey());
        regionDetail.setCountryDisplayName(countryEntry.getValue().getDisplayName());
        regionDetail.setName(regionDetailsEntry.getKey());
        regionByName.put(regionDetailsEntry.getKey(), regionDetail);
      }
    }
    return regionByName;
  }
}
