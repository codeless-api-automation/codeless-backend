package com.codeless.api.automation.entity;

public enum TestStatus {
  SUCCESS("SUCCESS"),
  FAIL("FAIL");

  private final String name;

  TestStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
