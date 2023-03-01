package com.fs.hc.fhir.core.resprocessor;

import com.fs.hc.fhir.core.util.IdGenerator;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceIDGenerator {
    @Autowired
    IdGenerator idGenerator;

    public void generateResourceId(Exchange exchange){
        IBaseResource resource = exchange.getIn().getBody(IBaseResource.class);

        //Generate ID from server side
        IIdType id = resource.getIdElement();
        id.setValue(""+idGenerator.nextId());

        exchange.getIn().setBody(resource);
    }
}
