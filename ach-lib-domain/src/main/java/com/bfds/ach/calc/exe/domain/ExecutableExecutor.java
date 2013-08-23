package com.bfds.ach.calc.exe.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * A Executor that executes a {@link Executable}.
 */
@Service
public class ExecutableExecutor extends AbstractExecutor<Executable> {

    private final Logger log = LoggerFactory.getLogger(ExecutableExecutor.class);

    @Override
    protected void doExecute(Executable calculation, Map<String, String> calculationParameters) {
        calculation.execute(calculationParameters);
    }
}
