package com.bfds.ach.calc.offenderValidation.batch;

import com.bfds.ach.calc.validation.domain.Offender;
import com.bfds.ach.calc.validation.domain.ValidationResult;

import java.util.List;


public interface ChunkPreProcessor  {

	
    void processOffenders(List<Offender> claimIds);

    List<ValidationResult> getValidationResults();

}
