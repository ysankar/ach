package com.bfds.ach.calc.exe.repositories;


import com.bfds.ach.calc.exe.domain.CalculationExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalculationExecutionRepository extends JpaRepository<CalculationExecution, Long> {

    List<CalculationExecution> findByExecutableId(String executableId);
}