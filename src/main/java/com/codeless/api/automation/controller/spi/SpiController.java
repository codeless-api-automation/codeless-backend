package com.codeless.api.automation.controller.spi;

import static com.codeless.api.automation.util.RestApiConstant.SPI_PATH;

import com.codeless.api.automation.dto.PutMetricRequest;
import com.codeless.api.automation.service.MetricService;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(SPI_PATH)
@Validated
@RequiredArgsConstructor
public class SpiController {

  private final MetricService metricService;

  @PostMapping(path = "/metrics/{scheduleId}")
  public void putMetric(
      @PathVariable @Valid @Size(min = 40, max = 40) String scheduleId,
      @RequestBody @Valid PutMetricRequest metricRequest) {

    // TODO: verify caller id in header (x-spi-api-key)
    // TODO: execute it async if possible, do not block on caller

    metricService.putMetrics(scheduleId, metricRequest);
  }
}
