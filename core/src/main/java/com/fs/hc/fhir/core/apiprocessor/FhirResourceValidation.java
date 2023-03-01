package com.fs.hc.fhir.core.apiprocessor;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import com.fs.hc.fhir.gateway.FsFhirGatewayProperties;
import com.fs.hc.fhir.gateway.exception.FhirResourceValidationException;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
public class FhirResourceValidation {
    @Autowired
    FhirValidator fhirValidator;
    @Autowired
    Properties resourceProfileProperties;
    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;

    public void validateResource(Exchange exchange) throws FhirResourceValidationException {
        Object body = exchange.getIn().getBody();
        IBaseResource baseResource;

        baseResource = body instanceof IBaseResource ? ((IBaseResource) body) : null;

        if (baseResource == null){
            //TODO throw an exception
            return;
        }

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
            String defaultProfileUrl = resourceProfileProperties.getProperty(resourceType);

            if (defaultProfileUrl == null || !fsFhirGatewayProperties.isDefaultProfileValidation()) {
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
}
