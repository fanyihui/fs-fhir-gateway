package com.fs.hc.fhir.component;

import org.apache.camel.Processor;
import org.apache.camel.support.component.AbstractApiConsumer;

import com.fs.hc.fhir.component.internal.trueApiName;

public class trueConsumer extends AbstractApiConsumer<trueApiName, trueConfiguration> {

    public trueConsumer(trueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

}
