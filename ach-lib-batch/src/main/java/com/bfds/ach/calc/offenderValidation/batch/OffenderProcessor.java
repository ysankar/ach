package com.bfds.ach.calc.offenderValidation.batch;

import com.google.common.base.Preconditions;
import com.bfds.ach.calc.validation.domain.Offender;
import com.bfds.ach.calc.validation.domain.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class OffenderProcessor implements ItemProcessor<Offender, List<ValidationResult>> {

    private final String processStatus;

    public OffenderProcessor(final String processStatus) {
        Preconditions.checkState(!StringUtils.isEmpty(processStatus), "processStatus is null");
        this.processStatus = processStatus;
    }

    /**
     *
     * @param offender is a claimant in offender database
     * @return This method returns a List of ValidationResults which hold claimid, validation type, status and comments
     * @throws Exception
     *
     * This method will identify the claimants that match with claimants in Offender Database
     * based on conditions firstname, lastname, ssn, tin and address
     */
    @Override
    public List<ValidationResult> process(Offender offender) throws Exception {

       return null;
    }



}
