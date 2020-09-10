package com.codeless.api.automation.controller;


import static com.codeless.api.automation.util.RestApiConstant.REGION_RESOURCE;

import com.codeless.api.automation.dto.request.Region;
import com.codeless.api.automation.service.RegionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(REGION_RESOURCE)
@Validated
@AllArgsConstructor
public class RegionController {

  private final RegionService regionService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Region> getAllRegions() {
    return regionService.getAllRegions();
  }
}
