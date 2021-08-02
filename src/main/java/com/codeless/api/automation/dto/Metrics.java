package com.codeless.api.automation.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metrics {

  private List<ResponsePoint> responsePoints;

}
