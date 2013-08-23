package com.bfds.ach.calc.exe.service;

import com.bfds.ach.calc.exe.domain.Calculation;
import com.bfds.ach.calc.exe.domain.CalculationExecution;
import com.bfds.ach.calc.exe.repositories.CalculationExecutionRepository;
import com.bfds.ach.calc.exe.repositories.CalculationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/test-ach-service.xml", "classpath:META-INF/spring/CalculationServiceIntegrationTest.xml"})
@TestExecutionListeners({com.ach.test.AchTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class CalculationServiceIntegrationTest {

    @Autowired
    private CalculationService calculationService;

    @Autowired
    @Qualifier("jpaTransactionManager")
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate txTemplate;

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private CalculationExecutionRepository calculationExecutionRepository;

    public void beforeAllTests() {
        txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Calculation calc = new Calculation.Factory()
                        .description("duplicate name calculation")
                        .calcType("Duplicate Name")
                        .executableId("duplicateNameValidator") // This is a spring bean in CalculationServiceIntegrationTest.xml
                        .get();
                calc.getCalculationParameters().put("param-1", "param-1-val");
                calc.getCalculationParameters().put("param-2", "param-2-val");
                calculationRepository.saveAndFlush(calc);

                calc = new Calculation.Factory()
                        .description("Claim Factor Assignment calculation")
                        .calcType("Claim Factor Assignment")
                        .executableId("claimFactorAssignment") // This is a spring bean in CalculationServiceIntegrationTest.xml
                        .get();
                calc.getCalculationParameters().put("param-1", "param-1-val");
                calculationRepository.saveAndFlush(calc);

                calc = new Calculation.Factory()
                        .description("Some calculation batch")
                        .calcType("spring batch")
                        .executableId("testCalcJob") // This is a spring bean in CalculationServiceIntegrationTest.xml
                        .get();
                calc.getCalculationParameters().put("param-1", "param-1-val");
                calculationRepository.saveAndFlush(calc);
            }
        });
    }

    public void afterAllTests() {
        txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                calculationRepository.delete(calculationRepository.findAll());
                calculationExecutionRepository.delete(calculationExecutionRepository.findAll());
            }
        });
    }


    @Test
    public void successfulCalculation() {
        Calculation calc =  calculationRepository.findByCalcType("Duplicate Name");
        calculationService.execute(calc.getId());
        validateLogCreationFor(calc, CalculationExecution.ExitStatus.COMPLETED);
    }

    @Test
    public void successfulSpringBatchCalculation() {
        Calculation calc =  calculationRepository.findByCalcType("spring batch");
        calculationService.execute(calc.getId());
        validateLogCreationFor(calc, CalculationExecution.ExitStatus.COMPLETED);
    }

    @Test
    public void failedCalculation() {
        Calculation calc =  calculationRepository.findByCalcType("Claim Factor Assignment");
        calculationService.execute(calc.getId());
        validateLogCreationFor(calc, CalculationExecution.ExitStatus.ERROR);
    }

    private void validateLogCreationFor(Calculation calc, CalculationExecution.ExitStatus exitStatus) {
        List<CalculationExecution> logs = calculationExecutionRepository.findByExecutableId(calc.getExecutableId());

        assertThat(logs).hasSize(1);

        CalculationExecution expected = newCalculationLog(calc.getCalcType(), calc.getExecutableId(),
                calc.getDescription(), calc.getCalculationParameters().toString(), exitStatus);

        CalculationExecution actual = logs.get(0);
        expected.setStartTime(actual.getStartTime());
        expected.setEndTime(actual.getEndTime());
        expected.setExitMessage(actual.getExitMessage());
        assertThat(actual).isEqualTo(expected);
    }

    private CalculationExecution newCalculationLog(String calcType, String executableId,
                                             String description, String calculationParameters, CalculationExecution.ExitStatus exitStatus) {
        CalculationExecution ret = new CalculationExecution();
        ret.setCalcType(calcType);
        ret.setExecutableId(executableId);
        ret.setDescription(description);
        ret.setCalculationParameters(calculationParameters);
        ret.setExitStatus(exitStatus);
        return ret;
    }

}
