package com.fs.hc.fhir.component;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.support.component.AbstractApiComponent;

import com.fs.hc.fhir.component.internal.trueApiCollection;
import com.fs.hc.fhir.component.internal.trueApiName;

@org.apache.camel.spi.annotations.Component("true")
public class trueComponent extends AbstractApiComponent<trueApiName, trueConfiguration, trueApiCollection> {

    public trueComponent() {
        super(trueEndpoint.class, trueApiName.class, trueApiCollection.getCollection());
    }

    public trueComponent(CamelContext context) {
        super(context, trueEndpoint.class, trueApiName.class, trueApiCollection.getCollection());
    }

    @Override
    protected trueApiName getApiName(String apiNameStr) throws IllegalArgumentException {
        return getCamelContext().getTypeConverter().convertTo(trueApiName.class, apiNameStr);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String methodName, trueApiName apiName,
                                      trueConfiguration endpointConfiguration) {
        trueEndpoint endpoint = new trueEndpoint(uri, this, apiName, methodName, endpointConfiguration);
        endpoint.setName(methodName);
        return endpoint;
    }

    /**
     * To use the shared configuration
     */
    @Override
    public void setConfiguration(trueConfiguration configuration) {
        super.setConfiguration(configuration);
    }

}
