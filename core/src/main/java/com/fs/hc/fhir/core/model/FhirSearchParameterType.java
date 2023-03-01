package com.fs.hc.fhir.core.model;

import org.hl7.fhir.exceptions.FHIRException;

public enum FhirSearchParameterType {
    NUMBER,
    DATE,
    STRING,
    TOKEN,
    REFERENCE,
    COMPOSITE,
    QUANTITY,
    URI,
    SPECIAL;

    private FhirSearchParameterType(){}

    public static FhirSearchParameterType fromCode(String codeString) throws FHIRException{
        if (codeString != null && !"".equals(codeString)) {
            if ("number".equals(codeString)) {
                return NUMBER;
            } else if ("date".equals(codeString)) {
                return DATE;
            } else if ("string".equals(codeString)) {
                return STRING;
            } else if ("token".equals(codeString)) {
                return TOKEN;
            } else if ("reference".equals(codeString)) {
                return REFERENCE;
            } else if ("composite".equals(codeString)) {
                return COMPOSITE;
            } else if ("quantity".equals(codeString)) {
                return QUANTITY;
            } else if ("uri".equals(codeString)) {
                return URI;
            } else if ("special".equals(codeString)) {
                return SPECIAL;
            } else {
                throw new FHIRException("Unknown SearchParamType code '" + codeString + "'");
            }
        } else {
            return null;
        }
    }

    public String toCode(){
        switch (this.ordinal()){
            case 1:
                return "number";
            case 2:
                return "date";
            case 3:
                return "string";
            case 4:
                return "token";
            case 5:
                return "reference";
            case 6:
                return "composite";
            case 7:
                return "quantity";
            case 8:
                return "uri";
            case 9:
                return "special";
            default:
                return "?";
        }
    }
}
