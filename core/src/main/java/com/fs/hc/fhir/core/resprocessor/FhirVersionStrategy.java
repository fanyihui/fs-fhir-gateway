package com.fs.hc.fhir.core.resprocessor;

import com.fs.hc.fhir.core.exception.FhirResourceValidationException;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FhirVersionStrategy {
    private Map<SupportedFhirVersionEnum, AbstractFhirResourceBuilder> fhirResourceBuilderMap = new HashMap<>();

    @Autowired
    public FhirVersionStrategy(List<AbstractFhirResourceBuilder> fhirResourceBuilderList){
        for (AbstractFhirResourceBuilder fhirResourceBuilder : fhirResourceBuilderList) {
            fhirResourceBuilderMap.put(fhirResourceBuilder.getFhirVersion(), fhirResourceBuilder);
        }
    }

    public AbstractFhirResourceBuilder getFhirResourceBuilder(SupportedFhirVersionEnum fhirVersion){
        return fhirResourceBuilderMap.get(fhirVersion);
    }

    public void validateFhirResource(SupportedFhirVersionEnum fhirVersionEnum, IBaseResource baseResource) throws FhirResourceValidationException {
        AbstractFhirResourceBuilder fhirResourceBuilder = getFhirResourceBuilder(fhirVersionEnum);
        fhirResourceBuilder.validateResource(baseResource);
    }
}
