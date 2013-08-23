package com.bfds.ach.calc.claim.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

/**
 *  this class is used to update the process_status of a claim
 *
 */
@Repository
public class ClaimJdbcRepository implements ClaimRepository, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ClaimJdbcRepository.class);
    
    private final  String PAYMENT_INELIGIBILITY_REASON = "deminimus";

    public static final String UPDATE_PROCESS_STATUS = "update claimant_claim set process_status = :processStatus where id = :claimId";
    
    public static final String UPDATE_PROCESS_STATUS_AND_NOT_ELIGIBLE_FOR_PAYMENT = "update cc " +
    		"set cc.process_status = " +
    		"  case " +
    		"      when cd.not_eligible_for_payment = 'false' " +
    		"      then :successStatus " +
    		"      else :errorStatus " +
    		"  end ," +
    		"     cc.not_eligible_for_payment =" +
    		"  case" +
    		"      when cd.not_eligible_for_payment = 'false'" +
    		"      then null" +
    		"      else 'true'" +
    		"  end, " +
    		"  cc.payment_inelig_reason =" +
    		"  case " +
    		"      when cd.not_eligible_for_payment = 'false'" +
    		"      then null " +
    		"      else :inEligibleReason" +
    		"  end " +
    		"from claimant_claim cc" +
    		"  right outer join claimant_distribution cd" +
    		"  on              cc.id                  = cd.claimant_claim_id " +
    		"  and             cd.distribution_type      = :distributionType " +
    		"  and             cd.deminimis_calculated   = 'true' " ;
    		 
    
    public static final String SELECT_IDS_SQL = "select id from claimant_claim where process_status = :processStatus ";


    @Autowired
    private DataSource dataSource;

    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**  
     * @see com.bfds.ach.calc.claim.repositories.ClaimRepository#updateProcessStatus(java.util.List)
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, value = "transactionManager")
    public void updateProcessStatus(final List<ClaimStatus> claimStatuses) {
        Preconditions.checkNotNull(claimStatuses, "claimStatuses is null");
        
        SqlParameterSource[] params = 
        		SqlParameterSourceUtils.createBatch(claimStatuses.toArray());
        
        namedParameterJdbcTemplate.batchUpdate(UPDATE_PROCESS_STATUS,params);
    }

    /**
     * Updates process status with success process status for claims which are eligible for payment.
     * i.e minimum one fund should be eligible for payment among all funds.
     * 
     * And updates process status with error process status and updates payment InEligible flag as 
     * true and reason as 'Deminimus' for claims which are not eligible for payment.
     * i.e none of the fund is eligible for payment.
     * 
     * @param successStatus
     * @param errorStatus
     * @param distributionType
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, value = "transactionManager")
    public void updateProcessStatusAndPaymentInEligible(final String successStatus, final String errorStatus, final String distributionType) {
    	
    	if(log.isInfoEnabled()) {
			log.info(String.format("Updating Claim process Status and Payment InEligible "));
        }
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("distributionType", distributionType);
    	params.addValue("errorStatus", errorStatus);
    	params.addValue("successStatus", successStatus);
    	params.addValue("inEligibleReason", PAYMENT_INELIGIBILITY_REASON);
    	
    	namedParameterJdbcTemplate.update(UPDATE_PROCESS_STATUS_AND_NOT_ELIGIBLE_FOR_PAYMENT, params);
    	
    }
    
    
    /**
     * Returns List of claim Id based on given Process status. 
     * 
     * @param processStatus
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY, value = "transactionManager")
    public List<Long> getIdsByProcessStatus(final String processStatus) {
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("processStatus", processStatus);
    	return namedParameterJdbcTemplate.queryForList(SELECT_IDS_SQL, params, Long.class);
    }
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(dataSource, "dataSource is null");
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

}
