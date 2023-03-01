package com.fs.hc.fhir.core.model;

public enum SupportedFhirVersionEnum {
    R4("4.0"), R5("5.0"), R4B("4.3");

    private String version;
    private SupportedFhirVersionEnum(String version){
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static SupportedFhirVersionEnum fromString(String text){
        if (text != null) {
            for (SupportedFhirVersionEnum b : SupportedFhirVersionEnum.values()) {
                if (text.equalsIgnoreCase(b.version)) {
                    return b;
                }
            }
        }
        return null;
    }
}
