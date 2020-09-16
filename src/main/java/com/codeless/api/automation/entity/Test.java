package com.codeless.api.automation.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tests")
@Data
public class Test {

  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false)
  private String name;
  @Lob
  @Column(nullable = false)
  private String json;
  @ManyToMany
  @JoinTable(
      name = "tests_executions",
      joinColumns = {@JoinColumn(name = "test_id")},
      inverseJoinColumns = {@JoinColumn(name = "execution_id")}
  )
  private Set<Execution> executions;
}
