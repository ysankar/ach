package com.bfds.ach.calc.exe.domain;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.fest.assertions.Assertions.assertThat;
@RunWith(MockitoJUnitRunner.class)
public class CalculationExecutorFactoryImplTest {

    private CalculationExecutorFactoryImpl calculationExecutorFactory = new CalculationExecutorFactoryImpl();

    private ApplicationContext applicationContext = mock(ApplicationContext.class);

    @Before
    public void before() {
        calculationExecutorFactory.setApplicationContext(applicationContext);
    }

    /**
     * Verify that the correct executor is returned.
     * @throws Exception
     */
    @Test
    public void getExecutor() throws Exception {
        when(applicationContext.getBeansOfType(Executor.class)).thenReturn(getExecutors());
        assertThat(calculationExecutorFactory.getExecutor(String.class)).isInstanceOf(StringExe.class);
        assertThat(calculationExecutorFactory.getExecutor(Long.class)).isInstanceOf(LongExe.class);
        assertThat(calculationExecutorFactory.getExecutor(Date.class)).isInstanceOf(DateExe.class);
        assertThat(calculationExecutorFactory.getExecutor(Integer.class)).isInstanceOf(NumberExe.class);
    }

    /**
     * Only one executor for a given type must exist.
     */
    @Test(expected = IllegalStateException.class)
    public void onlyOneExecutorForAGivenType() throws Exception {
        Map<String, Executor> executorMap =  getExecutors();
        //Now add a second executor for a type
        executorMap.put("bean-4", new StringExe());
        when(applicationContext.getBeansOfType(Executor.class)).thenReturn(executorMap);
        calculationExecutorFactory.getExecutor(String.class);
    }

    /**
     * When an executor for a type is not available an exception must be thrown.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionIfExecutorDoesNotExist() throws Exception {
        when(applicationContext.getBeansOfType(Executor.class)).thenReturn(getExecutors());
        // There is no executor for Double
        calculationExecutorFactory.getExecutor(Serializable.class);
    }

    private Map<String, Executor> getExecutors() {
        Map<String, Executor> ret = Maps.newHashMap();
        ret.put("bean-1", new StringExe());
        ret.put("bean-2", new LongExe());
        ret.put("bean-3", new DateExe());
        ret.put("bean-4", new NumberExe());
        return ret;
    }

    public static class StringExe extends AbstractExecutor<String> {
        public void doExecute(String calculation, Map<String, String> calculationParameters) { }
    }

    public static class LongExe extends AbstractExecutor<Long> {
        public void doExecute(Long calculation, Map<String, String> calculationParameters) { }
    }

    public static class DateExe implements Executor<Date> {
        public void execute(Date calculation, Map<String, String> calculationParameters) { }
        public void setListener(Listener listener) { }
    }

    public static class NumberExe implements Executor<Number> {
        public void execute(Number calculation, Map<String, String> calculationParameters) { }
        public void setListener(Listener listener) { }
    }
}
