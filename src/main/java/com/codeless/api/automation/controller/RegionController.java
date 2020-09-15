package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.REGION_RESOURCE;

import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.service.RegionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(REGION_RESOURCE)
@Validated
@AllArgsConstructor
public class RegionController {

  private final RegionService regionService;

  @GetMapping
  public List<Region> getAllRegions() {
    return regionService.getAllRegions();
  }
}
