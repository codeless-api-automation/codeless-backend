package com.codeless.api.automation.entity;

public enum ExecutionType {
  MANUAL_EXECUTION("MANUAL_EXECUTION"),
  SCHEDULED_EXECUTION("SCHEDULED_EXECUTION");

  private final String name;

  ExecutionType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
