package com.fs.hc.fhir.core.exception;

public class FhirReadDeletedResourceException extends Throwable {
    public FhirReadDeletedResourceException(){
        super();
    }

    public FhirReadDeletedResourceException(String message){
        super(message);
    }
}
