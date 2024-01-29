package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.appconfig.CountryConfig;
import com.codeless.api.automation.appconfig.CountryConfig.Country;
import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.appconfig.CountryConfigProvider;
import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.service.RegionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

  private final CountryConfigProvider countryConfigProvider;

  @Override
  public List<Region> getAllRegions() {
    List<Region> regions = new ArrayList<>();
    CountryConfig countryConfig = countryConfigProvider.getCountryConfig();
    for (Map.Entry<String, Country> countryEntry : countryConfig.getCountryByIso2Name().entrySet()) {
      Country country = countryEntry.getValue();
      for (Map.Entry<String, RegionDetails> regionEntry : country.getRegionByName().entrySet()) {
        RegionDetails regionDetails = regionEntry.getValue();
        regions.add(Region.builder()
            .country(country.getDisplayName())
            .iso2(countryEntry.getKey())
            .city(regionEntry.getKey())
            .defaultRegion(regionDetails.isDefaultRegion())
            .build());
      }
    }
    return regions;
  }


}
