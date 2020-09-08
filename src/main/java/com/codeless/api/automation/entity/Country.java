package com.codeless.api.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "countries")
@Data
public class Country {

  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String iso2;
  @Column
  private String displayName;
}
