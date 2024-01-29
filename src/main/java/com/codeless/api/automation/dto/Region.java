package com.codeless.api.automation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Region.RegionBuilder.class)
public class Region {

  @NotEmpty String iso2;
  @NotEmpty String country;
  @NotEmpty String city;
  boolean defaultRegion;

}