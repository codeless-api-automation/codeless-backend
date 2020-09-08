package com.codeless.api.automation.service.impl;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.dto.request.Region;
import com.codeless.api.automation.mapper.RegionMapper;
import com.codeless.api.automation.repository.RegionRepository;
import com.codeless.api.automation.service.RegionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService {

  @Autowired
  private RegionRepository regionRepository;
  @Autowired
  private RegionMapper regionToRegionDtoMapper;

  @Override
  public List<Region> getAllRegions() {
    return regionRepository.findAll().stream()
        .map(regionToRegionDtoMapper::map)
        .collect(toList());
  }
}
