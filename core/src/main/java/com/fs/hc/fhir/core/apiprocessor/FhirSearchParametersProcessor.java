package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.apiprocessor.search.FhirSearchUtil;
import com.fs.hc.fhir.core.exception.FhirSearchException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirSearchCondition;
import com.fs.hc.fhir.core.model.FhirSearchParameter;
import org.apache.camel.Exchange;
import org.apache.camel.util.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class FhirSearchParametersProcessor {
    @Autowired
    HashMap<String, HashMap<String, FhirSearchParameter>> searchParametersDef;

    public void processParameters(Exchange exchange) throws FhirSearchException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String uriElements[] = uri.split("/");
        String resourceType = uriElements[1];

        exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);

        List<FhirSearchCondition> searchConditions = new ArrayList<>();
        String searchBody = exchange.getIn().getBody(String.class);

        if (searchBody != null){
            searchConditions = FhirSearchUtil.generateSearchConditionsFromQueryParams(searchParametersDef, resourceType, searchBody, Charset.forName(exchange.getIn().getHeader(Exchange.CHARSET_NAME, String.class)));
        }

        exchange.getIn().setBody(searchConditions);
    }
}
