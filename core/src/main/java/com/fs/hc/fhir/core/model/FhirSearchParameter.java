package com.fs.hc.fhir.core.model;

import java.util.List;

public class FhirSearchParameter {
    private String name;
    private FhirSearchParameterType type;
    private String path;
    private List<String> referenceTypes;
    private String description;
    private String dataType;
    private String restrict;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FhirSearchParameterType getType() {
        return type;
    }

    public void setType(FhirSearchParameterType fhirSearchParameterType) {
        this.type = fhirSearchParameterType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getReferenceTypes() {
        return referenceTypes;
    }

    public void setReferenceTypes(List<String> referenceTypes) {
        this.referenceTypes = referenceTypes;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRestrict() {
        return restrict;
    }

    public void setRestrict(String restrict) {
        this.restrict = restrict;
    }
}
