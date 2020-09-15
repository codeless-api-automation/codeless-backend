package com.codeless.api.automation.mapper;

import com.codeless.api.automation.entity.Region;
import org.springframework.stereotype.Service;

@Service
public class RegionDtoMapper implements Mapper<com.codeless.api.automation.dto.Region, Region> {

  @Override
  public Region map(com.codeless.api.automation.dto.Region source) {
    Region region = new Region();
    region.setId(source.getId());
    region.setCity(source.getCity());
    region.setDefaultRegion(source.isDefaultRegion());
    return region;
  }
}
