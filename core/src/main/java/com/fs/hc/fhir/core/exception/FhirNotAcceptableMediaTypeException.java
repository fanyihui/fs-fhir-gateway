package com.fs.hc.fhir.core.exception;

public class FhirNotAcceptableMediaTypeException extends Throwable {
    public static int NOTACCEPTABLEMEDIATYPE = 406;

    public FhirNotAcceptableMediaTypeException(String message){
        super(message);
    }
}
