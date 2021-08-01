package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.service.ContentBuilderService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ContentBuilderServiceImpl implements ContentBuilderService {

  @Autowired private TemplateEngine templateEngine;

  @Override
  public String buildContent(String template, Map<String, String> context) {
    Context contextTemplate = new Context();
    context.forEach(contextTemplate::setVariable);
    return templateEngine.process(template, contextTemplate);
  }
}
