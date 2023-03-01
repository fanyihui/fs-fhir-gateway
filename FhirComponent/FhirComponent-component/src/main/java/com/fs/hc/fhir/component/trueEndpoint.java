package com.fs.hc.fhir.component;

import java.util.Map;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.component.AbstractApiEndpoint;
import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import com.fs.hc.fhir.component.api.trueHello;
import com.fs.hc.fhir.component.internal.trueApiCollection;
import com.fs.hc.fhir.component.internal.trueApiName;
import com.fs.hc.fhir.component.internal.trueConstants;
import com.fs.hc.fhir.component.internal.truePropertiesHelper;

/**
 * true component which does bla bla.
 *
 * TODO: Update one line description above what the component does.
 */
@UriEndpoint(firstVersion = "1.0.0.1", scheme = "true", title = "true", syntax="true:name", 
             category = {Category.API})
public class trueEndpoint extends AbstractApiEndpoint<trueApiName, trueConfiguration> {

    @UriPath @Metadata(required = true)
    private String name;

    // TODO create and manage API proxy
    private Object apiProxy;

    public trueEndpoint(String uri, trueComponent component,
                         trueApiName apiName, String methodName, trueConfiguration endpointConfiguration) {
        super(uri, component, apiName, methodName, trueApiCollection.getCollection().getHelper(apiName), endpointConfiguration);
    }

    public Producer createProducer() throws Exception {
        return new trueProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        // make sure inBody is not set for consumers
        if (inBody != null) {
            throw new IllegalArgumentException("Option inBody is not supported for consumer endpoint");
        }
        final trueConsumer consumer = new trueConsumer(this, processor);
        // also set consumer.* properties
        configureConsumer(consumer);
        return consumer;
    }

    @Override
    protected ApiMethodPropertiesHelper<trueConfiguration> getPropertiesHelper() {
        return truePropertiesHelper.getHelper(getCamelContext());
    }

    protected String getThreadProfileName() {
        return trueConstants.THREAD_PROFILE_NAME;
    }

    @Override
    protected void afterConfigureProperties() {
        // TODO create API proxy, set connection properties, etc.
        switch (apiName) {
            case HELLO:
                apiProxy = new trueHello();
                break;
            default:
                throw new IllegalArgumentException("Invalid API name " + apiName);
        }
    }

    @Override
    public Object getApiProxy(ApiMethod method, Map<String, Object> args) {
        return apiProxy;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
