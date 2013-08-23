package com.bfds.ach.calc.exe.domain;


import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * A Executor that executes a spring batch job.
 */
@Service
public class SpringBatchExecutor extends AbstractExecutor<Job> implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(SpringBatchExecutor.class);

    private static final String BATCH_RUN_ID = "batch_run_id";

    @Autowired
    protected JobLauncher jobLauncher;

    @Override
    protected void doExecute(Job calculationJob, Map<String, String> calculationParameters) throws Exception {
        Preconditions.checkNotNull(calculationJob, "calculationJob is null");
        JobExecution jobExecution = jobLauncher.run(calculationJob, asJobParameters(calculationParameters));
    }

    private JobParameters asJobParameters(Map<String, String> parameters) {
        JobParametersBuilder builder = new JobParametersBuilder();
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.addString(entry.getKey(), entry.getValue());
        }
        if(!parameters.containsKey(BATCH_RUN_ID)) {
            builder.addString(BATCH_RUN_ID, String.valueOf(System.currentTimeMillis()));
        }
        return builder.toJobParameters();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(jobLauncher, "jobLauncher is null");
    }
}
