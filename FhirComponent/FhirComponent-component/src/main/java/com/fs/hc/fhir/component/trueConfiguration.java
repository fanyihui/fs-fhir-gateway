package com.fs.hc.fhir.component;

import org.apache.camel.spi.Configurer;
import org.apache.camel.spi.ApiMethod;
import org.apache.camel.spi.ApiParam;
import org.apache.camel.spi.ApiParams;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

@ApiParams(apiName = "DEFAULT", description = "This is greeter",
        apiMethods = {
            @ApiMethod(methodName = "greetMe", signatures={"String greetMe(String name)"}),
            @ApiMethod(methodName = "greetUs", signatures={"String greetUs(String name1, String name2)"}),
            @ApiMethod(methodName = "sayHi", signatures={"String sayHi()"})},
        aliases = {})
@UriParams
@Configurer
public class trueConfiguration {

    @ApiParam(optional = false, apiMethods = {@ApiMethod(methodName = "greetMe")})
    private String name;

    @ApiParam(optional = false, apiMethods = {@ApiMethod(methodName = "greetUs")})
    private String name1;

    @ApiParam(optional = false, apiMethods = {@ApiMethod(methodName = "greetUs")})
    private String name2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

}
