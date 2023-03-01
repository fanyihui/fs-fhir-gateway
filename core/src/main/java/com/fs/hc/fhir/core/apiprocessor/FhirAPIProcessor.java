package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.exception.FhirAPIException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FhirOperationProcessor is defined to process all request from FHIR client. It includes following items;
 *
 * 1. Process FHIR version
 * 2. Process mime type from _format parameter, Accept header
 * 3. Process security rules
 * 4. Process login session
 * 5. Return accordinary response to client, include response code and response body
 *
 * */

@Component
public class FhirAPIProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FhirAPIProcessor.class);
    @Autowired
    FhirAPIUtil fhirAPIUtil;

    public void process(Exchange exchange) throws FhirAPIException {
        String mimeType = null;
        SupportedFhirVersionEnum supportedFhirVersionEnum = SupportedFhirVersionEnum.R4;

        //Initial FHIR related header to further processing
        exchange.getIn().setHeader(FhirConstant.FHIR_MIMETYPE_HEADER, mimeType);
        exchange.getIn().setHeader(FhirConstant.FHIR_VERSION_HEADER, supportedFhirVersionEnum);

        //Handle _format and Accept header
        Object format = exchange.getIn().getHeader("_format");
        if (format !=null ){
            logger.info("请求中的_format参数="+format.toString());
            mimeType = fhirAPIUtil.formalizeMimetype(format.toString());

            if (mimeType == null){
                throw new FhirAPIException(FhirAPIException.UNSUPPORTEDMEDIATYPE, "The mimetype "+ format.toString()+" is not supported.");
            }
        } else {
            String accept = exchange.getIn().getHeader("Accept").toString();
            logger.info("未指定_format参数，采用HTTP请求Accept头信息："+accept);
            String acceptProperties[] = accept.split(";");
            mimeType = fhirAPIUtil.formalizeMimetype(acceptProperties[0]);

            if (mimeType == null){
                String message = "The MimeType '" +accept +"' defined in the Accept Header is not acceptable.";
                throw new FhirAPIException(FhirAPIException.NOTACCEPTABLEMEDIATYPE, message);
            }

            //Get FHIR version information from client request (Accept Header: application/fhir+json; fhirVersion=4.0),
            //if no fhir version specified, then using the server default version defined in application.properties
            String fhirVersion = fhirAPIUtil.extractFhirVersion(accept);
            logger.info("客户端指定FHIR版本号："+fhirVersion);
            supportedFhirVersionEnum = SupportedFhirVersionEnum.fromString(fhirVersion);

            if (fhirVersion == null || supportedFhirVersionEnum == null){
                supportedFhirVersionEnum = SupportedFhirVersionEnum.R4;
            }
        }

        //update FHIR related header
        exchange.getIn().setHeader(FhirConstant.FHIR_MIMETYPE_HEADER, mimeType);
        exchange.getIn().setHeader(FhirConstant.FHIR_VERSION_HEADER, supportedFhirVersionEnum);
    }
}
