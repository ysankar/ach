package com.bfds.ach.calc.exe.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractExecutable implements Executable {

    private final Logger log = LoggerFactory.getLogger(AbstractExecutable.class);

    @Override
    public void execute(Map<String, String> validationParameters) {
        doExecute(validationParameters);
    }


    protected abstract void doExecute(Map<String, String> validationParameters);
}
