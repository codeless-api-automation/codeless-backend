package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.entity.Country;
import org.springframework.stereotype.Service;

@Service
public class RegionMapper implements Mapper<com.codeless.api.automation.entity.Region, Region> {

  @Override
  public Region map(com.codeless.api.automation.entity.Region source) {
    Country country = source.getCountry();
    return Region.builder()
        .id(source.getId())
        .city(source.getCity())
        .country(country.getDisplayName())
        .iso2(country.getIso2())
        .defaultRegion(source.isDefaultRegion())
        .build();
  }
}
