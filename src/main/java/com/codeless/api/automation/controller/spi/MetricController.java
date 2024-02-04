package com.codeless.api.automation.controller.spi;

import static com.codeless.api.automation.util.RestApiConstant.SPI_PATH;
import static com.codeless.api.automation.util.RestApiConstant.X_SPI_API_KEY;

import com.codeless.api.automation.dto.PutMetricRequest;
import com.codeless.api.automation.exception.ApiException;
import com.codeless.api.automation.service.MetricService;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(SPI_PATH)
@Validated
public class MetricController {

  private final String trustedCallerApiKey;
  private final MetricService metricService;

  public MetricController(
      @Value("${codeless.spi.trusted-caller-api-key}") String trustedCallerApiKey,
      @Autowired MetricService metricService) {
    this.trustedCallerApiKey = trustedCallerApiKey;
    this.metricService = metricService;
  }

  @PostMapping(path = "/metrics/{scheduleId}")
  public void putMetric(
      @PathVariable @Valid @Size(min = 40, max = 40) String scheduleId,
      @RequestBody @Valid PutMetricRequest metricRequest,
      @RequestHeader HttpHeaders headers) {
    List<String> xApiKey = headers.get(X_SPI_API_KEY);
    if (Objects.isNull(xApiKey) || xApiKey.size() != 1) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }
    if (!trustedCallerApiKey.equals(xApiKey.get(0))) {
      throw new ApiException("Unauthorized to access!", HttpStatus.UNAUTHORIZED.value());
    }
    metricService.putMetric(scheduleId, metricRequest);
  }
}
