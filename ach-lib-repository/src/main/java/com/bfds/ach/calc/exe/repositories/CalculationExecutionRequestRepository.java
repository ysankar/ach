package com.bfds.ach.calc.exe.repositories;


import com.bfds.ach.calc.exe.domain.CalculationExecutionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationExecutionRequestRepository extends JpaRepository<CalculationExecutionRequest, Long> {

}