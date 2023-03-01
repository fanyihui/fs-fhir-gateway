package com.fs.hc.fhir.core.exception;

public class InternalSystemException extends Throwable{
    public InternalSystemException(String message){
        super(message);
    }
    public InternalSystemException(Throwable throwable, String message){
        super(message, throwable);
    }
    public InternalSystemException(Throwable throwable){
        super(throwable);
    }
}
