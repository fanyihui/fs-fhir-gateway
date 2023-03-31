package com.fs.hc.fhir.core.apiprocessor;

import ca.uhn.fhir.parser.DataFormatException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.converter.stream.InputStreamCache;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FhirCreateProcessor {
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    public void createProcess(Exchange exchange) throws DataFormatException {
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        InputStreamCache inputStreamCache = exchange.getIn().getBody(InputStreamCache.class);
        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

        if (inputStreamCache == null){
            throw new DataFormatException("The body cannot be null.");
        }

        try{
            IBaseResource resource = fhirVersionStrategy.getFhirResourceBuilder(supportedFhirVersionEnum).decodeResource(mimeType, inputStreamCache);

            //Put version to the resource
            IBaseMetaType metaType = resource.getMeta();
            metaType.setVersionId("1");
            metaType.setLastUpdated(new Date());

            exchange.getIn().setBody(resource);
        } catch (DataFormatException dfe){
            //When the Resource Type is not recognized, then throw the exception.
            throw dfe;
        }

    }
}
