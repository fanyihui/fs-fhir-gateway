package com.fs.hc.fhir.component.internal;

import org.apache.camel.CamelContext;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import com.fs.hc.fhir.component.trueConfiguration;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for true component.
 */
public final class truePropertiesHelper extends ApiMethodPropertiesHelper<trueConfiguration> {

    private static truePropertiesHelper helper;

    private truePropertiesHelper(CamelContext context) {
        super(context, trueConfiguration.class, trueConstants.PROPERTY_PREFIX);
    }

    public static synchronized truePropertiesHelper getHelper(CamelContext context) {
        if (helper == null) {
            helper = new truePropertiesHelper(context);
        }
        return helper;
    }
}
