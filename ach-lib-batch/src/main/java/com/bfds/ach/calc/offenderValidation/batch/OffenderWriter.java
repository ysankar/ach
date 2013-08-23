package com.bfds.ach.calc.offenderValidation.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.bfds.ach.calc.validation.domain.Offender;
import com.bfds.ach.calc.validation.domain.ValidationResult;

public class OffenderWriter implements ItemWriter<List<Offender>>, InitializingBean {

	 private final Logger logger = LoggerFactory.getLogger(OffenderWriter.class);
	@Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private static final String PROCESS_STATUS_REVIEW = "Validation Review";

    private static final String QUERY_VALIDATION_RESULT_INSERT = "insert into claimant_validation (claimant_claim_id, validation_type, status, comments ) values (?, ?, ?, ?)";

    private static final String QUERY_CLAIMANT_CLAIM_STATUS_UPDATE = "update claimant_claim set process_status = ? where id = ?";

    private ChunkPreProcessor chunkPreProcessor;

    @Override
    public void write(List<? extends List<Offender>> lists) throws Exception {
        final Set<Long> claimIds = Sets.newHashSet();
        final List<ValidationResult> validationResultList = chunkPreProcessor.getValidationResults();
        for(ValidationResult validationResult : validationResultList) {
            if(claimIds.contains(validationResult.getClaimantClaimId())) {
                continue;
            }
            claimIds.add(validationResult.getClaimantClaimId());
        }
        updateProcessStatus(Lists.newArrayList(claimIds), PROCESS_STATUS_REVIEW);
        insertValidationResults(validationResultList);
    }

    /**
     *
     * @param claimIds are the ids of ClaimantClaims
     * @param processStatus is used to update the status of ClaimantClaim processstatus field
     * This method updates processstatus field of claimantclaim to Validation Review
     */
    private void updateProcessStatus(final List<Long> claimIds, final String processStatus) {
        Preconditions.checkNotNull(claimIds, "claimIds is null");
        Preconditions.checkNotNull(processStatus, "processStatus is null");

        if(logger.isDebugEnabled()){
    		logger.debug(String.format("Updating Process Status to %s for Claims: %s", processStatus, claimIds));
    	}

        jdbcTemplate.batchUpdate(QUERY_CLAIMANT_CLAIM_STATUS_UPDATE,new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final Long claimId = claimIds.get(i);
                ps.setString(1, processStatus);
                ps.setLong(2, claimId);

            }

            @Override
            public int getBatchSize() {
                return claimIds.size();
            }
        });
    }

    /**
     *
     * @param validationResultList
     *
     * This method inserts the ValidationResults into ClaimantValidation table
     */
    public void insertValidationResults(final List<ValidationResult> validationResultList){
        Preconditions.checkNotNull(validationResultList, "validationResultList is null");

        if(logger.isDebugEnabled()){
    		logger.debug(String.format("Inserting Validation Results into Claimant Validation table"));
    	}

        jdbcTemplate.batchUpdate(QUERY_VALIDATION_RESULT_INSERT,new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final ValidationResult validationResult = validationResultList.get(i);
                ps.setLong(1, validationResult.getClaimantClaimId());
                ps.setString(2, validationResult.getValidationType());
                ps.setString(3, validationResult.getStatus());
                ps.setString(4, validationResult.getComments());
            }

            @Override
            public int getBatchSize() {
                return validationResultList.size();
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(dataSource, "dataSource is null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setChunkPreProcessor(ChunkPreProcessor chunkPreProcessor) {
        this.chunkPreProcessor = chunkPreProcessor;
    }
}
