package com.fs.hc.fhir.core.resprocessor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import com.fs.hc.fhir.core.exception.FhirResourceValidationException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public abstract class AbstractFhirResourceBuilder {
    private FhirContext fhirContext;
    private FhirValidator fhirValidator;
    private HashMap<String, HashMap<String, List<String>>> compartmentDefinitionMap;
    private Properties defaultProfileProperties;
    private IParser jsonParser;
    private IParser xmlParser;

    public AbstractFhirResourceBuilder(FhirContext fhirContext, FhirValidator fhirValidator, HashMap<String, HashMap<String, List<String>>> compartmentDefinitionMap, Properties defaultProfileProperties){
        this.fhirContext = fhirContext;
        this.fhirValidator = fhirValidator;
        this.compartmentDefinitionMap = compartmentDefinitionMap;
        this.defaultProfileProperties = defaultProfileProperties;

        jsonParser = fhirContext.newJsonParser();
        xmlParser = fhirContext.newXmlParser();

        jsonParser.setParserErrorHandler(new StrictErrorHandler());
        jsonParser.setSuppressNarratives(false);
        jsonParser.setPrettyPrint(true);
        jsonParser.setSummaryMode(false);

        xmlParser.setParserErrorHandler(new StrictErrorHandler());
        xmlParser.setSuppressNarratives(false);
        xmlParser.setPrettyPrint(true);
        xmlParser.setSummaryMode(false);
    }

    public abstract SupportedFhirVersionEnum getFhirVersion();
    public abstract IBaseOperationOutcome createOperationOutcomeForException(String diagnosis, FhirIssueType fhirIssueType);
    public abstract IBaseOperationOutcome createOperationOutcomeForInfo(String info, FhirIssueType fhirIssueType);
    public abstract IBaseBundle createBundle(BundleTypeEnum bundleTypeEnum, List<IBaseResource> resourceList);

    public String encodeResource(String mimeType, IBaseResource resource) throws DataFormatException{
        return getParser(mimeType).encodeResourceToString(resource);
    }
    public IBaseResource decodeResource(String mimeType, InputStream inputStream) throws DataFormatException{
        IParser parser = getParser(mimeType);
        return parser.parseResource(inputStream);
    }
    public IBaseResource decodeResource(String mimeType, InputStreamCache body) throws DataFormatException{
        return getParser(mimeType).parseResource(new InputStreamReader(body));
    }
    public IBaseResource decodeResource(String mimeType, String body){
        return getParser(mimeType).parseResource(body);
    }
    public abstract void validateFhirResourceType(String resourceType) throws FHIRException;
    public void validateResource(IBaseResource baseResource) throws FhirResourceValidationException{
        ValidationResult validationResult = null;
        String resourceType = baseResource.fhirType();
        //Add code here to get profiles in the resource meta element.
        List profiles = null;
        IBaseMetaType meta = baseResource.getMeta();
        if (meta != null){
            profiles = meta.getProfile();
        }

        if ((profiles == null || profiles.size() == 0) ) {
            //Get the default profile defined in the configuration file for specific resource type
            //TODO if validate against default profile for a resource should be configured.
            String defaultProfileUrl = defaultProfileProperties.getProperty(resourceType);

            if (defaultProfileUrl == null) {
                validationResult = fhirValidator.validateWithResult(baseResource);
            } else {
                validationResult = fhirValidator.validateWithResult(baseResource, new ValidationOptions().addProfile(defaultProfileUrl));
            }
        } else {
            ValidationOptions validationOptions = new ValidationOptions();
            for (Object profileUri:profiles ) {
                //TODO if the profile in the meta is not support, need to be removed.
                validationOptions.addProfile(profileUri.toString());
            }
            validationResult = fhirValidator.validateWithResult(baseResource, validationOptions);
        }

        if (!validationResult.isSuccessful()){
            IBaseOperationOutcome operationOutcome = validationResult.toOperationOutcome();
            throw new FhirResourceValidationException(operationOutcome);
        }
    }
    public HashMap<String, HashMap<String, List<String>>> getCompartmentDefinitionMap(){
        return compartmentDefinitionMap;
    }

    private IParser getParser(String mimeType) throws DataFormatException {
        if (mimeType == null){
            throw new DataFormatException("Mime-type cannot be null");
        }
        if (mimeType.equals(FhirConstant.FHIRMIMETYPEJSON)){
            return jsonParser;
        } else if (mimeType.equals(FhirConstant.FHIRMIMETYPEXML)){
            return xmlParser;
        } else {
            throw new DataFormatException("MIME-TYPE '" + mimeType +"' is not supported");
        }
    }
}
