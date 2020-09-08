package com.codeless.api.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "regions")
@Data
public class Region {

  @Id
  @GeneratedValue
  private Long id;
  @OneToOne
  @JoinColumn(name = "country", referencedColumnName = "id")
  private Country country;
  @Column
  private String city;
  @Column
  private boolean defaultRegion;
}
