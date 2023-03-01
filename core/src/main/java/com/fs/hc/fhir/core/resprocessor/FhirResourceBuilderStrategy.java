package com.fs.hc.fhir.core.resprocessor;

import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FhirResourceBuilderStrategy {
    private Map<SupportedFhirVersionEnum, IFhirResourceBuilder> fhirResourceBuilderMap = new HashMap<>();

    @Autowired
    public FhirResourceBuilderStrategy(List<IFhirResourceBuilder> fhirResourceBuilderList){
        for (IFhirResourceBuilder fhirResourceBuilder : fhirResourceBuilderList) {
            fhirResourceBuilderMap.put(fhirResourceBuilder.getFhirVersion(), fhirResourceBuilder);
        }
    }

    public IFhirResourceBuilder getFhirResourceBuilder(SupportedFhirVersionEnum fhirVersion){
        return fhirResourceBuilderMap.get(fhirVersion);
    }
}
