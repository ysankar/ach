package com.bfds.ach.calc.exe.service;

import com.google.common.base.Preconditions;
import com.bfds.ach.calc.exe.domain.CalculationExecutionRequest;
import com.bfds.ach.calc.exe.repositories.CalculationExecutionRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Comparator;
import java.util.List;

/**
 * Reads {@link com.bfds.ach.calc.exe.domain.CalculationExecutionRequest}s from {@link com.bfds.ach.calc.exe.repositories.CalculationExecutionRequestRepository}
 * and submits them for execution to {@link CalculationService}.
 *
 */
public class CalculationExecutionRequestHandler implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(CalculationExecutionRequestHandler.class);

    @Autowired
    private CalculationExecutionRequestRepository executionRequestRepository;

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    @Qualifier("jpaTransactionManager")
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate txTemplate;

    /**
     * Picks the next {@link com.bfds.ach.calc.exe.domain.CalculationExecutionRequest} for processing.
     */
    //@Transactional(propagation = Propagation.REQUIRES_NEW, value = "jpaTransactionManager")
    public synchronized void processNextRequest() {
        final List<CalculationExecutionRequest> executionRequests =  executionRequestRepository.findAll();
        if(executionRequests.size() == 0) {
            log.debug("No calculation execution requests to process....................");
            return;
        }
        java.util.Collections.sort(executionRequests, new Comparator<CalculationExecutionRequest>() {
            @Override
            public int compare(CalculationExecutionRequest o1, CalculationExecutionRequest o2) {
                int ret = o1.getPriority() - o2.getPriority();
                if(ret == 0) {
                    //the ids will never be the same.
                    ret = o1.getId() - o2.getId() > 0 ? 1 : -1;
                }
                return ret;
            }
        });

        final CalculationExecutionRequest executionRequest = executionRequests.get(0);

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info(String.format("Executing calculation %s", executionRequest.getCalculation()));
                calculationService.execute(executionRequest.getCalculation().getId());
            }
        });
        txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                log.info(String.format("Removing calculation execution request %s", executionRequest));
                executionRequestRepository.delete(executionRequest);
                executionRequestRepository.flush();
            }
        });

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(executionRequestRepository, "executionRequestRepository is null");
        Preconditions.checkNotNull(calculationService, "calculationService is null");
        Preconditions.checkNotNull(taskExecutor, "taskExecutor is null");
        Preconditions.checkNotNull(transactionManager, "transactionManager is null");
        txTemplate = new TransactionTemplate(transactionManager);
    }
}
