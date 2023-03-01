package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.exception.FhirSearchException;
import com.fs.hc.fhir.core.exception.InternalSystemException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirSearchParameter;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.Exchange;
import org.apache.camel.util.IOHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class FhirReadCompartmentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FhirReadCompartmentProcessor.class);

    @Autowired
    FhirUtil fhirUtil;
    @Autowired
    HashMap<String, HashMap<String, List<String>>> compartmentDefinitionHashMap;
    @Autowired
    HashMap<String, HashMap<String, FhirSearchParameter>> searchParametersDef;

    public void readCompartmentProcessor(Exchange exchange) throws InternalSystemException, FhirSearchException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String uriElements[] = uri.split("/");
        String compartmentType = uriElements[1];

        String resourceType = exchange.getIn().getHeader("type", String.class);
        //String id = exchange.getIn().getHeader("id", String.class);

        //Check if the compartment and type is a valid FHIR defined compartments
        try{
            fhirUtil.validateFhirResourceType(exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class), resourceType);
            fhirUtil.validateFhirResourceType(exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class), compartmentType);
            exchange.getIn().setHeader(FhirConstant.FHIRCOMPARTMENTTYPE, compartmentType);
        } catch (InternalSystemException ise){
            logger.error(ise.getMessage(), ise);
            throw ise;
        }

        //Get the resource params from the definition and put them into a list.
        HashMap<String, List<String>> hashMap = compartmentDefinitionHashMap.get(compartmentType);
        List<String> params = hashMap.get(resourceType);
        //将所有的参数转换为查询参数，通过参数获取path，比如：参数patient-> path=subject -> path=subject.reference
        List<String> resourceParamsPath = new ArrayList<>();
        for (String para : params) {
            FhirSearchParameter fhirSearchParameter = searchParametersDef.get(resourceType).get(para);
            resourceParamsPath.add(fhirSearchParameter.getPath()+"."+"reference");
        }

        exchange.getIn().setHeader(FhirConstant.FHIRRESOURCEPARAMS, resourceParamsPath);

        //process all parameters (except _format, _pretty, _summary, _elements)
        String queryParams = exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class);
        List<FhirSearchCondition> fhirSearchConditions = FhirSearchUtil.generateSearchConditionsFromQueryParams(searchParametersDef,
                resourceType, queryParams, Charset.forName(IOHelper.getCharsetName(exchange)));

        exchange.getIn().setHeader(FhirConstant.FHIRSEARCHCONDITION, fhirSearchConditions);
    }
}
