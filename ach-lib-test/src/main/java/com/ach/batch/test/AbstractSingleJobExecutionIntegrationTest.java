package com.ach.batch.test;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Template method for all file import tests.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractSingleJobExecutionIntegrationTest {
    @Autowired
    protected JobLauncher jobLauncher;

    /**
     * One JobExecution for all tests. This way we can have a test verify only one(or a small set) of expectations
     * without having to run the same job multiple times.
     */
    protected static JobExecution jobExecution;

    /**
     * Run the job once and perform test on the results.
     */
    @org.junit.Before
    public  void before() throws Exception {
        if(getJobExecution() != null) {
            // Just one execution for all tests.
            return;
        }
        StopWatch sw = new StopWatch();
        sw.start();
        jobExecution = jobLauncher.run(geJOb(),
                getJobParameters());
        assertThat(getJobExecution().getExitStatus())
                .isEqualTo(ExitStatus.COMPLETED);
        System.out.println(getJobExecution().getExitStatus());
        System.out.println("Execution time : " + sw.getTime());
    }

    protected abstract Job geJOb();

    protected  abstract JobParameters getJobParameters();

    /**
     * Deleted all the file data from DB. Also the  jobExecution must be set to null for the next test class.
     */
    @AfterClass
    public static void after() {
        jobExecution = null;
    }

    /**
     *
     * @return The jobExecution against which all tests must/will run.
     */
    protected JobExecution getJobExecution() {
        return jobExecution;
    }
}

