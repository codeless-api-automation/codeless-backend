package com.codeless.api.automation.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Region.RegionBuilder.class)
public class Region {

  Long id;
  String iso2;
  String country;
  String city;
  boolean defaultRegion;

}