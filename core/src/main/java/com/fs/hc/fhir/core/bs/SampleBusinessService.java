package com.fs.hc.fhir.core.bs;

import com.fs.hc.fhir.core.exception.FhirAPIException;
import com.fs.hc.fhir.core.exception.FhirReadDeletedResourceException;
import com.fs.hc.fhir.core.exception.FhirUnknowResourceException;
import com.fs.hc.fhir.core.model.FhirSearchCondition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("businessService")
@ConditionalOnProperty(
        value = "com.fs.hc.fhir.gateway.businessService",
        havingValue = "com.fs.hc.fhir.core.bs.SampleBusinessService"
)
public class SampleBusinessService implements IBusinessService{
    @Override
    public IBaseResource createResource(IBaseResource baseResource) {
        return null;
    }

    @Override
    public IBaseResource readResourceById(String resourceType, String id) throws FhirReadDeletedResourceException, FhirUnknowResourceException {
        return null;
    }

    @Override
    public IBaseResource readResourceHistoryById(String resourceType, String id, String vid) throws FhirReadDeletedResourceException, FhirUnknowResourceException {
        return null;
    }

    @Override
    public List<IBaseResource> searchResources(String resourceType, List<FhirSearchCondition> fhirSearchConditions) {
        return null;
    }

    @Override
    public IBaseResource updateResource(IBaseResource baseResource, String id) throws FhirUnknowResourceException {
        return null;
    }

    @Override
    public void deleteResource(String resourceType, String id) throws FhirAPIException {

    }

    @Override
    public List<IBaseResource> searchCompartment(String compartmentType, String compartmentId, String resourceType, List<String> resourceParams, List<FhirSearchCondition> fhirSearchConditions) {
        return null;
    }
}
