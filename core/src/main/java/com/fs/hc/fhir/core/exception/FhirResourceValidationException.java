package com.fs.hc.fhir.core.exception;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

public class FhirResourceValidationException extends Throwable{
    private IBaseOperationOutcome operationOutcome = null;

    public FhirResourceValidationException(IBaseOperationOutcome operationOutcome){
        this.operationOutcome = operationOutcome;
    }

    public IBaseOperationOutcome getOperationOutcome() {
        return operationOutcome;
    }

    public void setOperationOutcome(IBaseOperationOutcome operationOutcome) {
        this.operationOutcome = operationOutcome;
    }
}
