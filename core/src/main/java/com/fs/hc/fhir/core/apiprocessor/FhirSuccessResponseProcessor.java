package com.fs.hc.fhir.core.apiprocessor;

import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import com.fs.hc.fhir.core.exception.InternalSystemException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import com.fs.hc.fhir.core.resprocessor.AbstractFhirResourceBuilder;
import com.fs.hc.fhir.core.util.DateTimeUtil;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FhirSuccessResponseProcessor {
    @Autowired
    FhirAPIUtil fhirAPIUtil;
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;


    public void createSuccessful(Exchange exchange) throws InternalSystemException {
        IBaseResource baseResource = null;
        //Resource resource = null;
        Object body = exchange.getIn().getBody();
        baseResource = body instanceof IBaseResource ? ((IBaseResource) body) : null;
        if (baseResource == null){
            throw new InternalSystemException("The body inside the Exchange is not a FHIR resource or null");
        }
        SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER).toString();

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "201");
        exchange.getMessage().setHeader(FhirConstant.FHIR_LOCATION_HEADER, baseResource.fhirType()+"/"+baseResource.getIdElement().getValue());
        exchange.getMessage().setBody(fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).encodeResource(mimeType, baseResource));
    }

    public void readSuccessfully(Exchange exchange) throws InternalSystemException{
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER).toString();
        SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

        IBaseResource baseResource = null;
        Object body = exchange.getIn().getBody();
        if (body != null) {
            baseResource = body instanceof IBaseResource ? ((IBaseResource) body) : null;
            if (baseResource == null) {
                throw new InternalSystemException("The body inside the Exchange is not a FHIR resource or null");
            }

            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
            exchange.getMessage().setHeader(FhirConstant.FHIR_VERSION_ETAG_HEADER, baseResource.getMeta().getVersionId());
            exchange.getMessage().setHeader(FhirConstant.FHIR_VERSION_LASTMODIFIER_HEADER, DateTimeUtil.convertInstant(baseResource.getMeta().getLastUpdated()));
            exchange.getMessage().setBody(fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).encodeResource(mimeType, baseResource));
        } else {
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
        }
    }

    public void searchSuccessfully(Exchange exchange) throws InternalSystemException{
        setExchangeOutForSearchSet(exchange);
    }

    public void updateSuccessfully(Exchange exchange) throws InternalSystemException{
        IBaseResource baseResource = null;

        Object body = exchange.getIn().getBody();
        baseResource = body instanceof IBaseResource ? ((IBaseResource) body) : null;
        if (baseResource == null){
            throw new InternalSystemException("The body inside the Exchange is not a FHIR resource or null");
        }

        SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER).toString();

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
        exchange.getMessage().setHeader(FhirConstant.FHIR_LOCATION_HEADER, baseResource.fhirType()+"/"+baseResource.getIdElement().getValue());
        //Set the version and lastUpdated to ETag and lastModified header
        exchange.getMessage().setHeader(FhirConstant.FHIR_VERSION_ETAG_HEADER, baseResource.getMeta().getVersionId());
        exchange.getMessage().setHeader(FhirConstant.FHIR_VERSION_LASTMODIFIER_HEADER, DateTimeUtil.convertInstant(baseResource.getMeta().getLastUpdated()));
        exchange.getMessage().setBody(fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).encodeResource(mimeType, baseResource));
    }

    public void deleteSuccessfully(Exchange exchange) throws InternalSystemException{
        // Check if the return include the deleted resource or not,
        // if not, return 204, if yes, return 200 with the resource as payload
        int statusCode = 204;
        boolean includePayload = exchange.getIn().getHeader(FhirConstant.INCLUDEPAYLOADFORDELETE, Boolean.class);

        if (includePayload){
            statusCode = 200;
            //IBaseResource baseResource = null;

            /*Object body = exchange.getIn().getBody();
            baseResource = body instanceof IBaseResource ? ((IBaseResource) body) : null;
            if (baseResource == null){
                throw new InternalSystemException("The body inside the Exchange is not a FHIR resource or null");
            }*/

            String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER).toString();
            String id = exchange.getIn().getHeader("id", String.class);
            String resourceType = exchange.getIn().getHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, String.class);
            SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

            IBaseOperationOutcome operationOutcome = fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).createOperationOutcomeForInfo(
                    "成功删除id为'"+id+"'的'"+resourceType+"'资源实例!",
                    FhirIssueType.INFORMATIONAL);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
            exchange.getMessage().setBody(fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).encodeResource(mimeType, operationOutcome));
        }

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, statusCode);
    }

    public void readCompartmentSuccessfully(Exchange exchange) throws InternalSystemException{
        setExchangeOutForSearchSet(exchange);
    }

    private void setExchangeOutForSearchSet(Exchange exchange) throws InternalSystemException{
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER).toString();
        SupportedFhirVersionEnum fhirVersionEnum = (SupportedFhirVersionEnum) exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);

        List<IBaseResource> resources = null;

        Object body = exchange.getIn().getBody();

        if (body != null){
            resources = body instanceof List ? ((List<IBaseResource>) body) : null;
            if (resources == null) {
                throw new InternalSystemException("The body inside the Exchange is not a FHIR resource or null");
            }
        }

        AbstractFhirResourceBuilder fhirResourceBuilder = fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum);

        IBaseBundle bundle = fhirResourceBuilder.createBundle(BundleTypeEnum.SEARCHSET ,resources);
        exchange.getMessage().setBody(fhirResourceBuilder.encodeResource(mimeType, bundle));
    }
}
