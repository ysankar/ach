package com.bfds.ach.calc.offenderValidation.batch;

import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.bfds.ach.calc.validation.domain.Offender;
import com.bfds.ach.calc.validation.domain.ValidationResult;


public class ChunkPreProcessorImpl implements ChunkPreProcessor, InitializingBean {

	private static final Logger  logger = LoggerFactory.getLogger(ChunkPreProcessorImpl.class);

    private static final String VALIDATION_FAIL_STATUS = "Fail";

    private final String processStatus;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private List<ValidationResult> validationResults = Lists.newArrayList();

    private static final String MATCH_CLAIMS_BY_ADDR =
            "select distinct cc.id \n" +
                    "from claimant_claim cc inner join claimant c on cc.claimant_fk = c.id \n" +
                    "inner join claimant_address ca on c.id = ca.claimant_fk\n" +
                    "and cc.process_status = ? ";


    private static final String MATCH_CLAIMS_BY_NAME =
            "select distinct cc.id \n" +
                    "from claimant_claim cc inner join claimant c on cc.claimant_fk = c.id \n" +
                    "and cc.process_status = ? ";

    public ChunkPreProcessorImpl(final String processStatus) {
        Preconditions.checkState(!StringUtils.isEmpty(processStatus), "processStatus is null");
        this.processStatus = processStatus;
    }

    @Override
    public void processOffenders(List<Offender> offenders) {
        validationResults.clear();
        if(offenders.size() == 0) { return;}
    	if(logger.isDebugEnabled()) {
            logger.debug(String.format("Identifying Claims of the claimants in Offender List%s",offenders ));
    	}
    	
        List<Long> offenderClaims  = getOffenderClaims(offenders);
        if(logger.isDebugEnabled()){
    		logger.debug(String.format("Found offenders: %s ",offenderClaims ));
    	}
        for(Long claimId :  offenderClaims) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.setClaimantClaimId(claimId);
            validationResult.setValidationType(OffenderValidationConstants.VALIDATION_TYPE);
            validationResult.setStatus(VALIDATION_FAIL_STATUS);
            validationResult.setComments(OffenderValidationConstants.VALIDATION_COMMENTS);
            validationResults.add(validationResult);
        }
    }

    /**
     * 
     * @param offenders is a list of Claimants in Offender Database
     * @return This method returns list of claimantclaimids
     * 
     * This method will identify the claimants present in Offender Database by
     * performing match on firstname & lastname, ssn/tin and addressLines
     */
    private List<Long> getOffenderClaims(List<Offender> offenders) {    	
        final List<String> params = Lists.newArrayList();
        params.add(processStatus);
        final StringBuilder sb = buildMatchByNameQuery(offenders, params);
        params.add(processStatus);
        sb.append("\n union \n").append(buildMatchByAddressQuery(offenders, params));
        return jdbcTemplate.queryForList(sb.toString(), Long.class, params.toArray());
    }


    private StringBuilder buildMatchByNameQuery(List<Offender> offenders, List<String> params) {
        final String nameMatchSql = "((upper(c.first_name) = upper(?) and upper(c.last_name) = upper(?)) or c.ssn = ? or c.tin = ?)\n";
        final StringBuilder sb = new StringBuilder(MATCH_CLAIMS_BY_NAME).append(" and ( ");
        for(Iterator<Offender> itr = offenders.iterator(); itr.hasNext();) {
            Offender offender = itr.next();
            params.add(offender.getFirstName());
            params.add(offender.getLastName());
            params.add(offender.getSsn());
            params.add(offender.getTin());
            sb.append(nameMatchSql);
            if(itr.hasNext()) {
                sb.append(" or ");
            }
        }

        return sb.append(")");
    }

    private StringBuilder buildMatchByAddressQuery(List<Offender> offenders, List<String> params) {

        final String addressMatchSql = "( isnull(upper(address1),'') = ? and isnull(upper(city),'') = ? and isnull(upper(state_code),'') = ?) ";

        
        final StringBuilder sb = new StringBuilder(MATCH_CLAIMS_BY_ADDR).append(" and ( ");
        for(Iterator<Offender> itr = offenders.iterator(); itr.hasNext();) {
            Offender offender = itr.next();
            params.add(StringUtils.defaultString(offender.getAddress().getAddress1()).toUpperCase());
            params.add(StringUtils.defaultString(offender.getAddress().getCity()).toUpperCase());
            params.add(StringUtils.defaultString(offender.getAddress().getStateCode()).toUpperCase());
            sb.append(addressMatchSql);
            if(itr.hasNext()) {
                sb.append(" or ");
            }
        }

        return sb.append(")");
    }

    

    @Override
    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(dataSource, "dataSource is null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
