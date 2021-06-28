package com.codeless.api.automation.mapper;

import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.entity.Country;
import com.codeless.api.automation.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionMapper implements Mapper<com.codeless.api.automation.entity.Region, Region> {

  private final CountryRepository countryRepository;

  @Override
  public Region map(com.codeless.api.automation.entity.Region source) {
    Long countryId = source.getCountryId();
    Country country = countryRepository.findById(countryId)
        .orElseThrow(RuntimeException::new);
    return Region.builder()
        .id(source.getId())
        .city(source.getCity())
        .countryId(countryId)
        .country(country.getDisplayName())
        .iso2(country.getIso2())
        .defaultRegion(source.isDefaultRegion())
        .build();
  }
}
