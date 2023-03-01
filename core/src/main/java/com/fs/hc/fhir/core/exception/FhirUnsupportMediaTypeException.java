package com.fs.hc.fhir.core.exception;

public class FhirUnsupportMediaTypeException extends Throwable {
    public static int UNSUPPORTEDMEDIATYPE = 415;

    public FhirUnsupportMediaTypeException() {
    }

    public FhirUnsupportMediaTypeException(String message) {
        super(message);
    }
}
