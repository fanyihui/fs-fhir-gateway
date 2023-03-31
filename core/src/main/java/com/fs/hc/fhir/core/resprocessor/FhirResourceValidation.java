package com.fs.hc.fhir.core.resprocessor;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import com.fs.hc.fhir.core.exception.FhirResourceValidationException;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class FhirResourceValidation {
    public static void validateResource(IBaseResource baseResource, FhirValidator fhirValidator, String profileUrl) throws FhirResourceValidationException {
        ValidationResult validationResult = null;
        String resourceType = baseResource.fhirType();

        //Add code here to get profiles in the resource meta element.
        List profiles = null;
        IBaseMetaType meta = baseResource.getMeta();
        if (meta != null){
            profiles = meta.getProfile();
        }

        if ((profiles == null || profiles.size() == 0) ) {
            if (profileUrl == null) {
                validationResult = fhirValidator.validateWithResult(baseResource);
            } else {
                validationResult = fhirValidator.validateWithResult(baseResource, new ValidationOptions().addProfile(profileUrl));
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
