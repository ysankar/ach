package com.bfds.ach.calc.exe.domain;


import java.util.Map;

/**
 * An interface that can be implemented by any class that can accept input a Map<String, String> and that does not return a value.
 * A class that implements this interface can have its output in a database.
 *
 * It is expected that an Executable can take a long time to process.
 * An implementation can execute asynchronously hence a successful return from the {@link #execute(java.util.Map)}
 * does not mean a successful completion of the executable.
 */
public interface Executable {

    /**
     *
     * @param validationParameters
     */
    void execute(Map<String, String> validationParameters);

}
