package com.codeless.api.automation.service;

import com.codeless.api.automation.domain.Test;
import java.util.List;

public interface TestSuiteBuilderService {

  String build(List<Test> tests);
}
