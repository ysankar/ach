package com.bfds.ach.calc.offenderValidation.batch;


import com.ach.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.ach.calc.Constants;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLException;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/ach-batch-test.xml", "classpath:META-INF/spring/calc-batch-validation-offender.xml"})
@TestExecutionListeners({com.ach.test.AchTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class OffenderDatabaseValidationPerfTest extends AbstractSingleJobExecutionIntegrationTest {

    @Autowired
    private Job offenderValidationJob;

    public void beforeAllTests() {

    }

    public void afterAllTests() {

    }

    @Override
    public void before() throws Exception {
        super.before();
    }

    @Override
    protected Job geJOb() {
        return offenderValidationJob;
    }

    @Override
    protected JobParameters getJobParameters() {
        return new JobParametersBuilder()
                .addString(Constants.PROCESS_STATUS, "OFFENDER CHECK")
                .addString(Constants.SUCCESS_PROCESS_STATUS, "OFFENDER CHECK PASS")
                .addString(Constants.BATCH_RUN_ID, System.currentTimeMillis()+"")
                .toJobParameters();
    }


    @Test
    public void test() throws SQLException{


    }
}
