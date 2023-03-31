package com.fs.hc.fhir.core.apiprocessor.search;

import com.fs.hc.fhir.core.model.FhirSearchParameterType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SearchParameterTypeSerializer implements JsonSerializer<FhirSearchParameterType>, JsonDeserializer<FhirSearchParameterType> {
    @Override
    public FhirSearchParameterType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = json.getAsString();

        return FhirSearchParameterType.fromCode(type);
    }

    @Override
    public JsonElement serialize(FhirSearchParameterType src, Type typeOfSrc, JsonSerializationContext context) {
        String type = src.toCode();
        return new JsonPrimitive(type);
    }
}
