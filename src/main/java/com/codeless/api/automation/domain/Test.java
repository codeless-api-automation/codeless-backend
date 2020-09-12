package com.codeless.api.automation.domain;

import java.util.List;
import lombok.Data;

@Data
public class Test {

  private String name;
  private String httpMethod;
  private String requestURL;
  private String requestBody;
  private List<Validator> validators;

}
