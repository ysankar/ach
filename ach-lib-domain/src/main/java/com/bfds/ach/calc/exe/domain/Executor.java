package com.bfds.ach.calc.exe.domain;


import java.util.Map;

/**
 * An executor similar to a {@link java.util.concurrent.Executor}. While {@link java.util.concurrent.Executor} executes a {@link Runnable}
 * a Executor can execute any type. There will be an implementatation of Executor for each type that can be executed.
 *
 * The input to be passed to the type instance must be passed as a {@link Map<String, String>} where the key is the name of the input parameter
 * and the value is the String representation of the value. The input can be stored in a relational database and hence the values cannot be types
 * that cannot be directly mapped to a SQL type. For now only String values are supported.
 *
 * @param <T>  The type to execute.
 */
public interface Executor<T> {
    /**
     * Executes the calculation. It is possible that the calculation will be executed asynchronously. So a
     * succesful return from this method does not imply that the calculation has completed.
     * @param calcBean - The calculation to execute.
     * @param calculationParameters - The input to the calculation.
     */
    void execute(T calcBean, Map<String, String> calculationParameters);

    void setListener(Listener listener);

    public interface Listener {
        void beforeExecution(Object calcBean, Map<String, String> calculationParameters);
        void afterExecution(Object calcBean, Map<String, String> calculationParameters);
        void executionError(Object calcBean, Map<String, String> calculationParameters, Throwable t);
    }
}
