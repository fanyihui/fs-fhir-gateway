package com.fs.hc.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;
import com.fs.hc.fhir.core.apiprocessor.search.SearchParameterTypeSerializer;
import com.fs.hc.fhir.core.model.FhirSearchParameter;
import com.fs.hc.fhir.core.model.FhirSearchParameterType;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR4BBuilder;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR4Builder;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR5Builder;
import com.fs.hc.fhir.core.util.IdGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CompartmentDefinition;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Configuration
public class FsFhirGatewayConfiguration implements ResourceLoaderAware {
    private final Logger logger = LoggerFactory.getLogger(FsFhirGatewayConfiguration.class);

    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;

    ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0",
            matchIfMissing = true
    )
    FhirContext fhirR4Context(){
        return FhirContext.forR4();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0",
            matchIfMissing = true
    )
    FhirValidator fhirValidatorR4() {
        return buildFhirValidator(fhirR4Context(), "profiles/r4");
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0"
    )
    FhirResourceR4Builder fhirResourceR4Builder(){
        return new FhirResourceR4Builder(fhirR4Context(), fhirValidatorR4(), compartmentDefinitionHashMapR4(), profilePropertiesR4());
    }


    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    FhirContext fhirR5Context(){
        return FhirContext.forR5();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    FhirValidator fhirValidatorR5(){
        return buildFhirValidator(fhirR5Context(), "profiles/r5");
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    FhirResourceR5Builder fhirResourceR5Builder(){
        return new FhirResourceR5Builder(fhirR5Context(), fhirValidatorR5(), compartmentDefinitionHashMapR5(), profilePropertiesR5());
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    FhirContext fhirR4BContext(){
        return FhirContext.forR4B();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    FhirValidator fhirValidatorR4B(){
        return buildFhirValidator(fhirR4BContext(), "profiles/r4b");
    }


    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    FhirResourceR4BBuilder fhirResourceR4BBuilder(){
        return new FhirResourceR4BBuilder(fhirR4BContext(), fhirValidatorR4B(), compartmentDefinitionHashMapR4B(), profilePropertiesR4B());
    }


    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0",
            matchIfMissing = true
    )
    Properties profilePropertiesR4() {
        return getDefaultProfilesTable("classpath:profiles/r4/profilemap.properties");
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    Properties profilePropertiesR4B(){
        return getDefaultProfilesTable("classpath:profiles/r4b/profilemap.properties");
    }
    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    Properties profilePropertiesR5(){
        return getDefaultProfilesTable("classpath:profiles/r5/profilemap.properties");
    }

    @Bean
    HashMap<String, HashMap<String, FhirSearchParameter>> searchParametersDef() {
        HashMap<String, HashMap<String, FhirSearchParameter>> hashMap = new HashMap<>();
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(FhirSearchParameterType.class, new SearchParameterTypeSerializer());

            Resource[] files = getFilesFromClasspath("search", "json");

            for (Resource file : files) {
                HashMap<String, HashMap<String, FhirSearchParameter>> hashMap1 = new HashMap<>();
                JsonReader jsonReader = new JsonReader(new InputStreamReader(file.getInputStream()));
                Gson gson = gsonBuilder.create();

                hashMap1 = gson.fromJson(jsonReader,
                        new TypeToken<HashMap<String, HashMap<String, FhirSearchParameter>>>() {
                        }.getType());
                hashMap.putAll(hashMap1);
                jsonReader.close();
            }
        } catch (IOException ie) {
            logger.error("System error", ie);
        }
        return hashMap;
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0",
            matchIfMissing = true
    )
    HashMap<String, HashMap<String, List<String>>> compartmentDefinitionHashMapR4() {
        return loadCompartmentDefinition(fhirR4Context(), "compartment/R4");
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    HashMap<String, HashMap<String, List<String>>> compartmentDefinitionHashMapR4B() {
        return loadCompartmentDefinition(fhirR4BContext(), "compartment/R4B");
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    HashMap<String, HashMap<String, List<String>>> compartmentDefinitionHashMapR5() {
        return loadCompartmentDefinition(fhirR5Context(), "compartment/R5");
    }

    @Bean
    IdGenerator idGenerator() {
        return new IdGenerator(0, 0);
    }

    private Resource[] getFilesFromClasspath(String path, String extension) {
        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
            return resolver.getResources("classpath:" + path + "/*." + extension);
        } catch (IOException ie) {
            logger.error("'" + path + "' is not in classpath", ie);
            return null;
        }
    }

    private FhirValidator buildFhirValidator(FhirContext fhirContext, String profileVersionDirectory){
        FhirValidator fhirValidator = fhirContext.newValidator();
        ValidationSupportChain validationSupportChain = new ValidationSupportChain();

        validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(fhirContext));
        validationSupportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(fhirContext));
        validationSupportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(fhirContext));
        validationSupportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(fhirContext));

        // Create a PrePopulatedValidationSupport which can be used to load custom
        // definitions.
        // load all StructureDefinitions, ValueSets, CodeSystems, etc.
        PrePopulatedValidationSupport prePopulatedValidationSupport = new PrePopulatedValidationSupport(fhirContext);

        IParser parser = fhirContext.newXmlParser();
        parser.setParserErrorHandler(new StrictErrorHandler());

        Resource[] files = getFilesFromClasspath(profileVersionDirectory+"/structuredef", "xml");
        try {
            for (Resource file : files) {
                logger.info("载入Profile定义 - " + file.getFilename());
                IBaseResource resource = parser.parseResource(file.getInputStream());
                if (!resource.fhirType().equals("StructureDefinition")) {
                    logger.warn("This is not a StructureDefinition", file);
                    continue;
                }

                prePopulatedValidationSupport.addStructureDefinition(resource);
            }
        } catch (DataFormatException | IOException fileNotFoundException) {
            fileNotFoundException.fillInStackTrace();
            logger.error(fileNotFoundException.getMessage(), fileNotFoundException);
        }

        try {
            Resource[] codeSystemFiles = getFilesFromClasspath(profileVersionDirectory+"/codesystems", "xml");

            if (codeSystemFiles != null) {
                for (Resource file : codeSystemFiles) {
                    logger.info("载入CodeSystem定义 - " + file.getFilename());
                    IBaseResource resource = parser.parseResource(file.getInputStream());
                    if (!resource.fhirType().equals("CodeSystem")) {
                        logger.warn("This is not a CodeSystem", file);
                        continue;
                    }

                    prePopulatedValidationSupport.addCodeSystem(resource);
                }
            }
        } catch (DataFormatException | IOException fileNotFoundException) {
            logger.error(fileNotFoundException.getMessage(), fileNotFoundException);
        }

        try {
            Resource[] valueSetFiles = getFilesFromClasspath(profileVersionDirectory+"/valuesets", "xml");

            if (valueSetFiles != null) {
                for (Resource file : valueSetFiles) {
                    logger.info("载入ValueSet定义 - " + file.getFilename());
                    IBaseResource resource = parser.parseResource(file.getInputStream());
                    if (!resource.fhirType().equals("ValueSet")) {
                        logger.warn("This is not a ValueSet", file);
                        continue;
                    }

                    prePopulatedValidationSupport.addValueSet(resource);
                }
            }
        } catch (DataFormatException | IOException fileNotFoundException) {
            logger.error(fileNotFoundException.getMessage(), fileNotFoundException);
        }

        validationSupportChain.addValidationSupport(prePopulatedValidationSupport);

        CachingValidationSupport cache = new CachingValidationSupport(validationSupportChain);
        FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(cache);
        fhirValidator.registerValidatorModule(fhirInstanceValidator);

        return fhirValidator;
    }

    private Properties getDefaultProfilesTable(String defaultProfileTablePath){
        Properties properties = new Properties();

        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
            //Resource resource = resolver.getResource("classpath:profiles/r4/profilemap.properties");
            Resource resource = resolver.getResource(defaultProfileTablePath);
            properties.load(resource.getInputStream());
        } catch (IOException ioException) {
            logger.error("Cannot find profilemap.properties file.", ioException);
        }

        return properties;
    }

    private HashMap<String, HashMap<String, List<String>>> loadCompartmentDefinition(FhirContext fhirContext, String filePath){
        Resource[] files = getFilesFromClasspath(filePath, "json");
        HashMap<String, HashMap<String, List<String>>> compartmentDefinitionHashMap = new HashMap<>();

        IParser parser = fhirContext.newJsonParser();
        parser.setParserErrorHandler(new StrictErrorHandler());

        for (Resource file : files) {
            try {
                IBaseResource resource = parser.parseResource(file.getInputStream());
                if (resource.fhirType().equals("CompartmentDefinition")) {
                    HashMap<String, List<String>> hashMap = new HashMap<>();
                    CompartmentDefinition compartmentDefinition = (CompartmentDefinition) resource;
                    String code = compartmentDefinition.getCode().toCode();
                    List<CompartmentDefinition.CompartmentDefinitionResourceComponent> components = compartmentDefinition
                            .getResource();
                    for (CompartmentDefinition.CompartmentDefinitionResourceComponent component : components) {
                        String resourceType = component.getCode();
                        List<StringType> params = component.getParam();
                        List<String> searchParams = new ArrayList<>();
                        for (StringType stringType : params) {
                            searchParams.add(stringType.toString());
                        }
                        hashMap.put(resourceType, searchParams);
                    }
                    compartmentDefinitionHashMap.put(code, hashMap);
                }
            } catch (IOException ie) {
                logger.error(ie.getMessage(), ie);
            }
        }
        return compartmentDefinitionHashMap;
    }
}
