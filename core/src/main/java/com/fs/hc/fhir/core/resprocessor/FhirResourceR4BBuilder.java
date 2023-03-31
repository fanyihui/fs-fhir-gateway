package com.fs.hc.fhir.core.resprocessor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.validation.FhirValidator;
import com.fs.hc.fhir.core.exception.FhirResourceValidationException;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Resource;
import org.hl7.fhir.r4b.model.OperationOutcome;
import org.hl7.fhir.r4b.model.ResourceType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class FhirResourceR4BBuilder extends AbstractFhirResourceBuilder {
    public FhirResourceR4BBuilder(FhirContext fhirContext, FhirValidator fhirValidator, HashMap<String, HashMap<String, List<String>>> compartmentDefinitionMap, Properties defaultProfileProperties) {
        super(fhirContext, fhirValidator, compartmentDefinitionMap, defaultProfileProperties);
    }

    @Override
    public SupportedFhirVersionEnum getFhirVersion() {
        return SupportedFhirVersionEnum.R4B;
    }

    @Override
    public void validateFhirResourceType(String resourceType) throws FHIRException {
        ResourceType.fromCode(resourceType);
    }

    @Override
    public IBaseOperationOutcome createOperationOutcomeForException(String diagnosis, FhirIssueType fhirIssueType) {
        return createOperationOutcome(diagnosis, OperationOutcome.IssueSeverity.ERROR, fhirIssueType);
    }

    @Override
    public IBaseOperationOutcome createOperationOutcomeForInfo(String info, FhirIssueType fhirIssueType) {
        return createOperationOutcome(info, OperationOutcome.IssueSeverity.INFORMATION, fhirIssueType);
    }

    @Override
    public IBaseBundle createBundle(BundleTypeEnum bundleTypeEnum, List<IBaseResource> resourceList) {
        Bundle bundle = new Bundle();
        int total = (resourceList!=null) ? resourceList.size() : 0;
        bundle.setTotal(total);
        bundle.setType(Bundle.BundleType.fromCode(bundleTypeEnum.getCode()));

        Iterator<IBaseResource> resourceIterator = resourceList.iterator();
        while (resourceIterator.hasNext()){
            IBaseResource resource = resourceIterator.next();
            Bundle.BundleEntryComponent entryComponent = new Bundle.BundleEntryComponent();

            Bundle.BundleEntryResponseComponent responseComponent = new Bundle.BundleEntryResponseComponent();
            responseComponent.setStatus("200");
            entryComponent.setResponse(responseComponent);
            entryComponent.setResource((Resource) resource);

            bundle.addEntry(entryComponent);
        }

        return bundle;
    }

    private IBaseOperationOutcome createOperationOutcome(String message, OperationOutcome.IssueSeverity issueSeverity, FhirIssueType fhirIssueType){
        OperationOutcome operationOutcome = new OperationOutcome();
        OperationOutcome.OperationOutcomeIssueComponent component = new OperationOutcome.OperationOutcomeIssueComponent();
        component.setSeverity(issueSeverity);
        component.setCode(OperationOutcome.IssueType.fromCode(fhirIssueType.toCode()));
        component.setDiagnostics(message);
        operationOutcome.addIssue(component);
        return operationOutcome;
    }
}
