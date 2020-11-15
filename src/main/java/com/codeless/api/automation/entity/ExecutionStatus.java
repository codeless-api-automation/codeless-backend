package com.codeless.api.automation.entity;

public enum ExecutionStatus {
  PENDING("PENDING"),
  STARTED("STARTED"),
  COMPLETED("COMPLETED");

  private final String name;

  ExecutionStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
