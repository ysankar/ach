package com.bfds.ach.calc.exe.domain;

/**
 * A factory for {@link Executor}.
 */
public interface CalculationExecutorFactory {

    /**
     * Finds a suitable {@link Executor} the can execute instances of type beanClass.
     * @param beanClass - The type of the calculation that requires an executor.
     * @return   - A {@link Executor} that can execute instances of type beanClass.
     */
    Executor getExecutor(Class beanClass);
}
