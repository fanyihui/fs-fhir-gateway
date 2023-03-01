package com.fs.hc.fhir.core.exception;

import com.fs.hc.fhir.core.model.FhirIssueType;

public class FhirAPIException extends Throwable {
    public static int UNSUPPORTEDMEDIATYPE = 415;
    public static int NOTACCEPTABLEMEDIATYPE = 406;
    public static int BADREQUEST = 400;
    public static int NOTAUTHORIZED = 401;
    public static int NOTFOUND = 404;
    public static int METHODNOTALLOWED = 405;
    public static int UNPROCESSABLEENTITY = 422;
    public static int CONFLICT = 409;

    private int httpStatusCode = 404;
    private FhirIssueType fhirIssueType = FhirIssueType.EXCEPTION;

    public FhirAPIException(int httpStatusCode){
        this.httpStatusCode = httpStatusCode;
    }

    public FhirAPIException(int httpStatusCode, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public FhirAPIException(int httpStatusCode, FhirIssueType fhirIssueType, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.fhirIssueType = fhirIssueType;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public FhirIssueType getFhirIssueType() {
        return fhirIssueType;
    }

    public void setFhirIssueType(FhirIssueType fhirIssueType) {
        this.fhirIssueType = fhirIssueType;
    }
}
