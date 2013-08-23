package com.bfds.ach.calc.exe.domain;


import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractExecutor<T> implements Executor<T> {

    private final Logger log = LoggerFactory.getLogger(AbstractExecutor.class);

    private Listener listener;

    @Override
    public void execute(T calculation, Map<String, String> calculationParameters) {
        try {
            log.info(String.format("executing calculation %s with parameters %s", calculation, calculationParameters));
            notifyBeforeExecutionListeners(calculation, calculationParameters);
            doExecute(calculation, calculationParameters);
            log.info(String.format("completed executing calculation %s with parameters %s", calculation, calculationParameters));
            notifyAfterExecutionListeners(calculation, calculationParameters);
        }catch(Exception e) {
            log.error(String.format("Error running calculation %s with parameters %s", calculation, calculationParameters) , e);
            notifyExecutionErrorListeners(calculation, calculationParameters, e);
        }
    }

    private void notifyExecutionErrorListeners(T calculation, Map<String, String> calculationParameters, Exception e) {
        if(listener != null) {
            listener.executionError(calculation, calculationParameters, e);
        }
    }

    private void notifyAfterExecutionListeners(T calculation, Map<String, String> calculationParameters) {
        if(listener != null) {
            listener.afterExecution(calculation, calculationParameters);
        }
    }

    private void notifyBeforeExecutionListeners(T calculation, Map<String, String> calculationParameters) {
        if(listener != null) {
            listener.beforeExecution(calculation, calculationParameters);
        }
    }

    @Override
    public void setListener(Listener listener) {
        Preconditions.checkNotNull(listener, "listener is null");
        this.listener = listener;
    }

    protected abstract void  doExecute(T calculation, Map<String, String> calculationParameters) throws Exception;
}
