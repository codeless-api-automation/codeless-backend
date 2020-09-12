package com.codeless.api.automation.domain;

import java.util.List;
import lombok.Data;

@Data
public class Validator {

  private String dslName;
  private String predicate;
  private List<ValidatorAttribute> inputFields;
}
