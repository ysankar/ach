package com.bfds.ach.calc.exe.service;

/**
 * Client interface to the calculation subsystem.
 */
public interface CalculationService {
    /**
     * Executes a calculation. How a calculation for the given calculationId is obtained is an implementation detail.
     * The default implementation looks in the database.
     * @param calculationId - The system wide unique id of the calculation.
     */
    void execute(Long calculationId);
}
