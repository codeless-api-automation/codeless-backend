package com.codeless.api.automation.service.impl;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.mapper.RegionMapper;
import com.codeless.api.automation.repository.RegionRepository;
import com.codeless.api.automation.service.RegionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

  private final RegionRepository regionRepository;
  private final RegionMapper regionToRegionDtoMapper;

  @Override
  public List<Region> getAllRegions() {
    return regionRepository.findAll().stream()
        .map(regionToRegionDtoMapper::map)
        .collect(toList());
  }
}
