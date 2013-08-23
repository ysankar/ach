package com.bfds.ach.calc.offenderValidation.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.base.Preconditions;


public class NonOffenderClaimProcessor implements Tasklet, InitializingBean {

    private final String processStatus;
    private final String successProcessStatus;

    private static final String VALIDATION_PASS_STATUS = "Pass";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DataSource dataSource;


    public NonOffenderClaimProcessor(final String processStatus, final String successProcessStatus) {
        this.processStatus = processStatus;
        this.successProcessStatus = successProcessStatus;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("successProcessStatus", successProcessStatus);
        params.addValue("processStatus", processStatus);

        namedParameterJdbcTemplate.update("insert into claimant_validation (claimant_claim_id, validation_type, status, comments ) " +
                " select distinct  id, '"+OffenderValidationConstants.VALIDATION_TYPE+"', '"+VALIDATION_PASS_STATUS+"',  '"+OffenderValidationConstants.VALIDATION_COMMENTS +"' " +
                " from claimant_claim where process_status = :processStatus", params);

        namedParameterJdbcTemplate.update("update claimant_claim set process_status = :successProcessStatus where process_status = :processStatus", params);
    	

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(dataSource, "dataSource is null");
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
