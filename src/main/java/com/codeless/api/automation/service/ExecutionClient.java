package com.codeless.api.automation.service;

import java.util.Map;

public interface ExecutionClient {

  void execute(String region, Map<String, String> payload);

}
