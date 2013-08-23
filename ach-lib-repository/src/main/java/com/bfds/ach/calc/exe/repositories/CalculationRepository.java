package com.bfds.ach.calc.exe.repositories;


import com.bfds.ach.calc.exe.domain.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {

    Calculation findByCalcType(String calcType);

}