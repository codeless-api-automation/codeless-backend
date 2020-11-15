package com.codeless.api.automation.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "executions")
@Data
public class Execution {

  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String name;
  @Enumerated(EnumType.ORDINAL)
  @Column
  private ExecutionType type;
  @Enumerated(EnumType.ORDINAL)
  @Column
  private ExecutionStatus status;
  @Column
  private Long executionId;
  @OneToOne
  @JoinColumn(name = "region", referencedColumnName = "id")
  private Region region;
  @ManyToMany(targetEntity = Test.class)
  @JoinTable(name = "tests_executions",
      joinColumns = {@JoinColumn(name = "execution_id")},
      inverseJoinColumns = {@JoinColumn(name = "test_id")})
  private List<Test> tests;
  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinTable(name = "execution_result",
      joinColumns =
          {@JoinColumn(name = "execution_id")},
      inverseJoinColumns =
          {@JoinColumn(name = "result_id", referencedColumnName = "execution_id")})
  private Result result;
}
