package com.codeless.api.automation.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TestRepository extends JpaRepository<Test, Integer> {}
