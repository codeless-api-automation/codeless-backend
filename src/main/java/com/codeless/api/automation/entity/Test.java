package com.codeless.api.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tests")
@Data
public class Test {

  @Id
  @GeneratedValue
  private Integer id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String json;
}
