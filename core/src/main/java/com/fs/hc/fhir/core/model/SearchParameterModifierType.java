package com.fs.hc.fhir.core.model;

import org.hl7.fhir.exceptions.FHIRException;

public enum SearchParameterModifierType {
    MISSING,
    EXACT,
    CONTAINS,
    TEXT,
    NOT,
    ABOVE,
    BELOW,
    IN,
    NOT_IN,
    OF_TYPE;

    public static SearchParameterModifierType fromCode(String modifier) throws FHIRException {
        if (modifier != null && !"".equals(modifier)) {
            if (modifier.equals("missing")){
                return MISSING;
            } else if (modifier.equals("exact")){
                return EXACT;
            } else if (modifier.equals("contains")){
                return CONTAINS;
            } else if (modifier.equals("text")){
                return TEXT;
            } else if (modifier.equals("not")){
                return NOT;
            } else if (modifier.equals("above")){
                return ABOVE;
            } else if (modifier.equals("below")){
                return BELOW;
            } else if (modifier.equals("in")){
                return IN;
            } else if (modifier.equals("not-in")){
                return NOT_IN;
            } else if (modifier.equals("of-type")){
                return OF_TYPE;
            } else {
                throw new FHIRException("Unacceptable modifier '" + modifier+ "'");
            }
        } else {
            return null;
        }
    }

    public String toCode(){
        switch (this.ordinal()){
            case 1:
                return "missing";
            case 2:
                return "exact";
            case 3:
                return "contains";
            case 4:
                return "text";
            case 5:
                return "not";
            case 6:
                return "above";
            case 7:
                return "below";
            case 8:
                return "in";
            case 9:
                return "not-in";
            case 10:
                return "of-type";
            default:
                return "?";
        }
    }
}
