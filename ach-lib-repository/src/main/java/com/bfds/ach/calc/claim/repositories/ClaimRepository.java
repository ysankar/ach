package com.bfds.ach.calc.claim.repositories;


import com.google.common.base.Preconditions;
import org.springframework.util.StringUtils;

import java.util.List;

public interface ClaimRepository {

    /**
     * Updates the process status of the claims.
     * @param claimStatuses a list which holds the claimId's and status for those
     */
    void updateProcessStatus(List<ClaimStatus> claimStatuses);
    
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
    void updateProcessStatusAndPaymentInEligible(final String successStatus, final String errorStatus,final String distributionType);
    
    
    /**
     * Returns List of claim Id based on given Process status; 
     * 
     * @param processStatus
     * @return
     */
    List<Long> getIdsByProcessStatus(final String processStatus);

    class ClaimStatus {
        private final Long claimId;
        private final String processStatus;

        public ClaimStatus(Long claimId, String processStatus) {
            Preconditions.checkNotNull(claimId, "claimId is null");
            Preconditions.checkState(StringUtils.hasText(processStatus), "processStatus is null or empty for claimId %s", claimId);
            this.claimId = claimId;
            this.processStatus = processStatus;
        }

        public Long getClaimId() {
            return claimId;
        }

        public String getProcessStatus() {
            return processStatus;
        }

        @Override
        public String toString() {
            return "ClaimStatus{" +
                    "claimId=" + claimId +
                    ", processStatus='" + processStatus + '\'' +
                    '}';
        }
    }
}