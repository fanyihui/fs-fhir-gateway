package com.fs.hc.fhir.core.resprocessor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

public class FhirResourceR4BBuilder implements IFhirResourceBuilder{
    private FhirContext fhirR4BContext;
    private IParser jsonParser;
    private IParser xmlParser;

    public FhirResourceR4BBuilder(FhirContext fhirContext) {
        this.fhirR4BContext = fhirContext;
        jsonParser = fhirR4BContext.newJsonParser();
        xmlParser = fhirR4BContext.newXmlParser();

        jsonParser.setParserErrorHandler(new StrictErrorHandler());
        jsonParser.setSuppressNarratives(false);
        jsonParser.setPrettyPrint(true);
        jsonParser.setSummaryMode(false);

        xmlParser.setParserErrorHandler(new StrictErrorHandler());
        xmlParser.setSuppressNarratives(false);
        xmlParser.setPrettyPrint(true);
        xmlParser.setSummaryMode(false);
    }

    @Override
    public SupportedFhirVersionEnum getFhirVersion() {
        return SupportedFhirVersionEnum.R4B;
    }

    @Override
    public IBaseOperationOutcome createOperationOutcomeForException(String diagnosis, FhirIssueType fhirIssueType) {
        return null;
    }

    @Override
    public IBaseOperationOutcome createOperationOutcomeForInfo(String info, FhirIssueType fhirIssueType) {
        return null;
    }

    @Override
    public IBaseBundle createBundle(BundleTypeEnum bundleTypeEnum, List<IBaseResource> resourceList) {
        return null;
    }

    @Override
    public String encodeResource(String mimeType, IBaseResource resource) {
        return null;
    }

    @Override
    public IBaseResource decodeResource(String mimeType, InputStream inputStream) {
        return null;
    }

    @Override
    public IBaseResource decodeResource(String mimeType, InputStreamCache body) {
        return null;
    }

    @Override
    public IBaseResource decodeResource(String mimeType, String body) {
        return null;
    }

    @Override
    public void validateFhirResourceType(String resourceType) throws FHIRException {

    }
}
