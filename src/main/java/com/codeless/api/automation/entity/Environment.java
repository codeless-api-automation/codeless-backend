package com.codeless.api.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "environments")
@Data
public class Environment {

  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String name;
}
