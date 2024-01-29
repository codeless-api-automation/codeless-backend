package com.codeless.api.automation.util;

import com.codeless.api.automation.appconfig.CountryConfig.RegionDetails;
import com.codeless.api.automation.context.CronExpressionContext;
import com.codeless.api.automation.dto.Region;
import com.codeless.api.automation.dto.Timer;
import java.util.Map;

public final class ObjectBuilder {

  public static Region buildRegion(
      String regionName,
      Map<String, RegionDetails> regionByName) {
    RegionDetails regionDetails = regionByName.get(regionName);
    return Region.builder()
        .country(regionDetails.getCountryDisplayName())
        .iso2(regionDetails.getCountryIso2Name())
        .defaultRegion(regionDetails.isDefaultRegion())
        .city(regionName)
        .build();
  }

  public static CronExpressionContext buildCronExpressionContext(Timer source) {
    return CronExpressionContext.builder()
        .type(source.getType())
        .minute(source.getMinute())
        .hour(source.getHour())
        .week(source.getWeek())
        .time(source.getTime())
        .build();
  }
}
