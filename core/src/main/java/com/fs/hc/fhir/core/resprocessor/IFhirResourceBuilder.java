package com.fs.hc.fhir.core.resprocessor;

import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.InputStream;
import java.util.List;

public interface IFhirResourceBuilder {
    public SupportedFhirVersionEnum getFhirVersion();
    public IBaseOperationOutcome createOperationOutcomeForException(String diagnosis, FhirIssueType fhirIssueType);
    public IBaseOperationOutcome createOperationOutcomeForInfo(String info, FhirIssueType fhirIssueType);
    public IBaseBundle createBundle(BundleTypeEnum bundleTypeEnum, List<IBaseResource> resourceList);
    public String encodeResource(String mimeType, IBaseResource resource);
    public IBaseResource decodeResource(String mimeType, InputStream inputStream);
    public IBaseResource decodeResource(String mimeType, InputStreamCache body);
    public IBaseResource decodeResource(String mimeType, String body);
    public void validateFhirResourceType(String resourceType) throws FHIRException;
}
