package com.fs.hc.fhir.core.model;

import org.hl7.fhir.exceptions.FHIRException;

public enum FhirSearchParameterPrefix {
    EQ,
    NE,
    GT,
    LT,
    GE,
    LE,
    SA,
    EB,
    AP;

    public static FhirSearchParameterPrefix fromCode(String prefix) throws FHIRException {
        if (prefix != null && !prefix.equals("")){
            if (prefix.equals("eq")){
                return EQ;
            } else if (prefix.equals("ne")){
                return NE;
            } else if (prefix.equals("gt")){
                return GT;
            } else if (prefix.equals("lt")){
                return LT;
            } else if (prefix.equals("ge")){
                return GE;
            } else if (prefix.equals("le")){
                return LE;
            } else if (prefix.equals("sa")){
                return SA;
            } else if (prefix.equals("eb")){
                return EB;
            } else if (prefix.equals("ap")){
                return AP;
            } else {
                throw new FHIRException("Unknown prefix '"+prefix+"'");
            }
        } else {
            return null;
        }
    }

    public String toCode(){
        switch (this.ordinal()){
            case 1:
                return "eq";
            case 2:
                return "ne";
            case 3:
                return "gt";
            case 4:
                return "lt";
            case 5:
                return "ge";
            case 6:
                return "le";
            case 7:
                return "sa";
            case 8:
                return "eb";
            case 9:
                return "ap";
            default:
                return "?";
        }
    }
}
