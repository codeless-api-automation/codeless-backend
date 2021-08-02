package com.codeless.api.automation.util;

public final class RestApiConstant {

  public static final String ROOT_PATH = "/codeless";

  public static final String TEST_RESOURCE = "/tests";
  public static final String SINGLE_TEST_RESOURCE =  TEST_RESOURCE + "/";
  public static final String SINGLE_TEST_RESOURCE_WITH_ROOT_PATH = ROOT_PATH + SINGLE_TEST_RESOURCE;

  public static final String EXECUTION_RESOURCE = "/executions";
  public static final String SCHEDULES_RESOURCE = "/schedules";
  public static final String REGION_RESOURCE = "/regions";
  public static final String RESULT_RESOURCE = "/results";
  public static final String METRICS_RESOURCE = "/metrics";
  public static final String AUTH_RESOURCE = "/auth";

  private RestApiConstant() {
  }
}
