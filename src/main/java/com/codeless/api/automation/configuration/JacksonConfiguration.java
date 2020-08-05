package com.codeless.api.automation.configuration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

  @Bean
  @Primary
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder()
        .findModulesViaServiceLoader(true);
    mapperBuilder.annotationIntrospector(new JacksonAnnotationIntrospector() {
      @Override
      public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
        if (ac.hasAnnotation(
            JsonPOJOBuilder.class)) {
          return super.findPOJOBuilderConfig(ac);
        }
        return new JsonPOJOBuilder.Value("build", "");
      }
    });
    return mapperBuilder;
  }
}
