package com.fs.hc.fhir.core.apiprocessor;

import ca.uhn.fhir.parser.DataFormatException;
import com.fs.hc.fhir.gateway.FsFhirGatewayProperties;
import com.fs.hc.fhir.gateway.exception.FhirAPIException;
import com.fs.hc.fhir.gateway.exception.InternalSystemException;
import com.fs.hc.fhir.gateway.util.FhirUtil;
import com.fs.hc.fhir.gateway.util.SupportedFhirVersionEnum;
import org.apache.camel.Exchange;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirUpdateProcessor {
    @Autowired
    FhirUtil fhirUtil;

    @Autowired
    FsFhirGatewayProperties config;

    public void updateProcess(Exchange exchange) throws FhirAPIException, InternalSystemException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String uriElements[] = uri.split("/");
        String resourceType = uriElements[1];

        if (resourceType != null){
            try{
                fhirUtil.validateFhirResourceType(exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class), resourceType);
            } catch (InternalSystemException ise){
                throw ise;
            }
            //Put the resourceType to the header, so other routes or process can use this header to determine which FHIR Resource is processing
            exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }

        String id = exchange.getIn().getHeader("id", String.class);
        if (id == null){
            throw new FhirAPIException(FhirAPIException.BADREQUEST, "The incoming request is not a update API. It doesn't include id value in the url. The uri should be follwo the format http://base/[resoureType]/[id]");
        }

        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        InputStreamCache inputStreamCache = exchange.getIn().getBody(InputStreamCache.class);

        if (inputStreamCache == null){
            throw new DataFormatException("The body cannot be null.");
        }

        try{
            IBaseResource resource = fhirUtil.decodeResource(mimeType, inputStreamCache);

            //resource.getIdElement().getIdPart()
            IIdType iIdType = resource.getIdElement();
            String idPart = iIdType.getIdPart();

            if (!id.equals(idPart)){
                throw new FhirAPIException(FhirAPIException.BADREQUEST, "The id in the uri is different from the id assigned in the resource.");
            }

            exchange.getIn().setBody(resource);
        } catch (DataFormatException dfe){
            throw dfe;
        }
    }
}
