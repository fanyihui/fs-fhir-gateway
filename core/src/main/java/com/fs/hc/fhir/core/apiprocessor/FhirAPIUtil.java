package com.fs.hc.fhir.core.apiprocessor;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.fs.hc.fhir.core.model.FhirConstant;
import org.springframework.stereotype.Component;

@Component
public class FhirAPIUtil {
    private final String validXmlMimetype = "xml, text/xml, application/xml, application/fhir+xml";
    private final String validJsonMimetype = "json, text/json, application/json, application/fhir+json";

    public String extractFhirVersion(String accept){
        String fhirVersion = null;

        if (accept != null && accept.lastIndexOf("fhirVersion") != -1){
            int lo = accept.lastIndexOf("fhirVersion");
            String fhirVersionProperty = accept.substring(lo);
            String[] property = fhirVersionProperty.split("=");
            fhirVersion = property[1];
        }

        return fhirVersion;
    }

    public String formalizeMimetype(String mimeType){
        if (mimeType == null){
            return null;
        }

        String type = null;

        mimeType = mimeType.replace(' ', '+');

        if (validXmlMimetype.indexOf(mimeType) != -1){
            type = FhirConstant.FHIRMIMETYPEXML;
        } else if (validJsonMimetype.indexOf(mimeType) != -1){
            type = FhirConstant.FHIRMIMETYPEJSON;
        } else {
            return null;
        }

        return type;
    }

    public String getResourceTypeFromUri(String uri){
        String uriElements[] = uri.split("/");
        String resourceType = uriElements[1];
        return resourceType;
    }
}
