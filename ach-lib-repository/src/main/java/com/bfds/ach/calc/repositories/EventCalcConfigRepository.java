package com.bfds.ach.calc.repositories;


import com.bfds.ach.calc.domain.EventCalcConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCalcConfigRepository extends JpaRepository<EventCalcConfig, Long> {
}