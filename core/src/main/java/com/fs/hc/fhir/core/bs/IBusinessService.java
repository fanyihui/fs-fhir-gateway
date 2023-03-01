package com.fs.hc.fhir.core.bs;

import com.fs.hc.fhir.core.exception.FhirAPIException;
import com.fs.hc.fhir.core.exception.FhirReadDeletedResourceException;
import com.fs.hc.fhir.core.exception.FhirUnknowResourceException;
import com.fs.hc.fhir.core.model.FhirSearchCondition;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface IBusinessService {
        public IBaseResource createResource(IBaseResource baseResource);
        public IBaseResource readResourceById(String resourceType, String id) throws FhirReadDeletedResourceException, FhirUnknowResourceException;
        public IBaseResource readResourceHistoryById(String resourceType, String id, String vid) throws FhirReadDeletedResourceException, FhirUnknowResourceException;
        public List<IBaseResource> searchResources(String resourceType, List<FhirSearchCondition> fhirSearchConditions);
        public IBaseResource updateResource(IBaseResource baseResource, String id) throws FhirUnknowResourceException;
        public void deleteResource(String resourceType, String id) throws FhirAPIException;
        public List<IBaseResource> searchCompartment(String compartmentType, String compartmentId, String resourceType, List<String> resourceParams, List<FhirSearchCondition> fhirSearchConditions);
}
