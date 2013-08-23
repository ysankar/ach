package com.bfds.ach.calc.exe.service;


import com.google.common.base.Preconditions;
import com.bfds.ach.calc.exe.domain.Calculation;
import com.bfds.ach.calc.exe.domain.CalculationExecution;
import com.bfds.ach.calc.exe.domain.CalculationExecutorFactory;
import com.bfds.ach.calc.exe.domain.Executor;
import com.bfds.ach.calc.exe.repositories.CalculationExecutionRepository;
import com.bfds.ach.calc.exe.repositories.CalculationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An implementation of  CalculationService that looks up the caliculation in the {@link CalculationRepository}
 *
 * @see com.bfds.ach.calc.exe.domain.Executor
 */
@Service
public class CalculationServiceImpl implements CalculationService, InitializingBean, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(CalculationServiceImpl.class);
    /**
     * All calculations are expected to have corresponding beans in the application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The execution of the spring bean corresponding to the calculation is delegated to a {@link com.bfds.ach.calc.exe.domain.Executor}.
     * executorFactory is used to find a suitable executor.
     */
    @Autowired
    private CalculationExecutorFactory executorFactory;

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private CalculationExecutionRepository calculationExecutionRepository;

    @Autowired
    @Qualifier("jpaTransactionManager")
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate txTemplate;

    /**
     * Execute the calculation uniquely identifed by calculationId.
     * Once the calculation is obtained it's {@link Calculation#getExecutableId()} should correspond to
     * a bean in the {@link #applicationContext}. The corresponding bean is then executed via a suitable {@link com.bfds.ach.calc.exe.domain.Executor}.
     * @param calculationId - The system wide unique id of the calculation.
     */
    @Override
    public void execute(final Long calculationId) {
        final Calculation calc = calculationRepository.findOne(calculationId);
        Preconditions.checkState(calc != null, "No entity of type %s with id:%s found in repository.", Calculation.class, calculationId);
        final Object calcBean = applicationContext.getBean(calc.getExecutableId());
        final Executor calculationExecutor = executorFactory.getExecutor(calcBean.getClass());
        Map<String, String> calculationParameters = calc.getCalculationParameters() == null ? Collections.<String, String>emptyMap() : calc.getCalculationParameters();
        calculationExecutor.setListener(new ExecutorListener(calc));
        calculationExecutor.execute(calcBean, calculationParameters );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(applicationContext, "applicationContext is null");
        Preconditions.checkNotNull(calculationRepository, "calculationRepository is null");
        Preconditions.checkNotNull(executorFactory, "executorFactory is null");
        Preconditions.checkNotNull(transactionManager, "transactionManager is null");
        txTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setCalculationRepository(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    public void setExecutorFactory(CalculationExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * An {@link Executor.Listener] that logs to the database.
     */
    private class ExecutorListener implements Executor.Listener {

        private final Calculation calculation;

        private CalculationExecution execution;

        /**
         * Allow the concurrent execution of the same executable. By default it is false.
         */
        private boolean allowConcurrentExecution;

        public ExecutorListener(final Calculation calculation) {
            Preconditions.checkNotNull(calculation, "calculation is null");
            this.calculation = calculation;
        }

        @Override
        public void beforeExecution(Object calcBean, Map<String, String> calculationParameters) {
            if(isRunning(calculation.getExecutableId())) {
                throw new IllegalStateException(String.format("Cannot start calculation:%s. It is currently running.", calculation.getId()));
            }
            txTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    execution = new CalculationExecution(calculation);
                    execution.setStartTime(new Date());
                    calculationExecutionRepository.saveAndFlush(execution);
                }
            });
        }

        @Override
        public void afterExecution(Object calcBean, Map<String, String> calculationParameters) {
            txTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    execution.setEndTime(new Date());
                    execution.setExitStatus(CalculationExecution.ExitStatus.COMPLETED);
                    calculationExecutionRepository.saveAndFlush(execution);
                }
            });
        }

        @Override
        public void executionError(Object calcBean, Map<String, String> calculationParameters, final Throwable t) {
            txTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    execution.setEndTime(new Date());
                    execution.setExitStatus(CalculationExecution.ExitStatus.ERROR);
                    execution.setExitMessage(getErrorMessage(t));
                    calculationExecutionRepository.saveAndFlush(execution);
                }
            });
        }

        /**
         * Is the executable currenty running.
         * @param executableId  - The Id of the executable.
         * @return true if the executable is currently running else false.
         */
        private boolean isRunning(final String executableId) {
            List<CalculationExecution> calculationExecutions = calculationExecutionRepository.findByExecutableId(executableId);
            for(CalculationExecution calculationExecution : calculationExecutions) {
                if(calculationExecution.isRunning()) {
                    log.info("Calcualiotn is in progess for executableId: %s. ", executableId);
                    return true;
                }
            }
            return false;
        }
        
        //TODO: This method is copied form org.springframework.batch.item.file.mapping.FileRecordLineMapperDecorator.
        private String getErrorMessage(final Throwable e) {
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String ret =  sw.getBuffer().toString();
            if(ret.length() > 5000) {
                ret = ret.substring(0, 4999);
            }
            return ret;
        }
    }
}
