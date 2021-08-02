package com.codeless.api.automation.service;

import java.util.Map;

public interface ContentBuilderService {

  String buildContent(String template, Map<String, String> context);
}
