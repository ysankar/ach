package com.bfds.ach.calc.offenderValidation.batch;


import com.bfds.ach.calc.Constants;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

/**
 * Validates the parameters required for the job.
 */
public class OffenderValidationJobParametersValidator extends DefaultJobParametersValidator {


    public OffenderValidationJobParametersValidator() {
        super(new String[] {Constants.PROCESS_STATUS,  Constants.SUCCESS_PROCESS_STATUS},
        		new String[] {Constants.BATCH_RUN_ID});
    }


}
