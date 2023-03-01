package com.fs.hc.fhir.core.exception;

public class FhirSearchException extends Throwable {
    private int errorCode = 400;
    public FhirSearchException(int code, String message){
        super(message);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
