package com.fs.hc.fhir.component;

import org.apache.camel.support.component.AbstractApiProducer;

import com.fs.hc.fhir.component.internal.trueApiName;
import com.fs.hc.fhir.component.internal.truePropertiesHelper;

public class trueProducer extends AbstractApiProducer<trueApiName, trueConfiguration> {

    public trueProducer(trueEndpoint endpoint) {
        super(endpoint, truePropertiesHelper.getHelper(endpoint.getCamelContext()));
    }
}
