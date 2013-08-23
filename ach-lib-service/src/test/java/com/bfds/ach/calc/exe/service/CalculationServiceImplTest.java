package com.bfds.ach.calc.exe.service;

import com.google.common.collect.Maps;
import com.bfds.ach.calc.exe.domain.Calculation;
import com.bfds.ach.calc.exe.domain.CalculationExecutorFactory;
import com.bfds.ach.calc.exe.domain.Executable;
import com.bfds.ach.calc.exe.domain.Executor;
import com.bfds.ach.calc.exe.repositories.CalculationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculationServiceImplTest {

    private CalculationServiceImpl calculationService = new CalculationServiceImpl();

    private ApplicationContext applicationContext = mock(ApplicationContext.class);

    private CalculationRepository calculationRepository = mock(CalculationRepository.class);

    private CalculationExecutorFactory executorFactory = mock(CalculationExecutorFactory.class);

    private PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);

    @Before
    public void before() throws Exception{
        calculationService.setApplicationContext(applicationContext);
        calculationService.setCalculationRepository(calculationRepository);
        calculationService.setExecutorFactory(executorFactory);
        calculationService.setTransactionManager(transactionManager);
        calculationService.afterPropertiesSet();// This line is just to increase code coverage :)
    }
    /**
     * Throw an Exception when a non existent calculation id is passed for execution.
     */
    @Test(expected = IllegalStateException.class)
    public void nonExistentCalculationId() {
        when(calculationRepository.findOne(anyLong())).thenReturn(null);
        calculationService.execute(1L);
    }

    @Test
    public void execute() {
        TestExecutable exe = new TestExecutable();
        Calculation ret = newCalculation();
        Executor<TestExecutable> executor = mock(Executor.class);
        when(calculationRepository.findOne(anyLong())).thenReturn(ret);
        when(applicationContext.getBean(ret.getExecutableId())).thenReturn(exe);
        when(executorFactory.getExecutor(TestExecutable.class)).thenReturn(executor);
        calculationService.execute(1L);
        verify(calculationRepository).findOne(anyLong());
        verify(applicationContext).getBean(newCalculation().getExecutableId());
        verify(executorFactory).getExecutor(TestExecutable.class);
        verify(executor).execute(exe, ret.getCalculationParameters());

    }

    private Calculation newCalculation() {
        Map<String, String> params = Maps.newHashMap();
        params.put("param-1", "param-1-val");
        Calculation ret = new Calculation.Factory()
                .calcType("calctype-1")
                .executableId("exe-1")
                .calculationParameters(params)
                .description("desc-1")
                .get();
        return ret;
    }

    public static class TestExecutable implements  Executable {
        @Override
        public void execute(Map<String, String> validationParameters) {

        }
    }



}
