package com.fs.hc.fhir.core.apiprocessor.search;

import com.fs.hc.fhir.core.exception.FhirSearchException;
import com.fs.hc.fhir.core.model.FhirSearchCondition;
import com.fs.hc.fhir.core.model.FhirSearchParameter;
import com.fs.hc.fhir.core.model.FhirSearchParameterType;
import com.fs.hc.fhir.core.util.StringUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FhirSearchUtil {
    public final static String MISSING = "missing";
    public final static String EXACT = "exact";
    public final static String CONTAINS = "contains";
    public final static String TEXT = "text";
    public final static String IN = "in";
    public final static String NOT = "not";
    public final static String NOT_IN = "not-in";
    public final static String BELOW = "below";
    public final static String ABOVE = "above";
    public final static String OF_TYPE = "of-type";
    public final static String DATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public final static String YEARFORMATER = "yyyy";
    public final static String YEARMONTHFORMATER = "yyyy-MM";
    public final static String DATEFORMATER = "yyyy-MM-dd";

    private final static BigDecimal DIVIDE = new BigDecimal("2");

    public static FhirSearchCondition generateSearchConditionForString(FhirSearchParameter searchParameter,
                                                                String modifier,
                                                                String value) throws FhirSearchException{
        FhirSearchCondition searchCondition = new FhirSearchCondition();

        if (modifier != null) {
            //For String, only accept missing, exact and contains as modifier
            if (modifier.equals(MISSING) || modifier.equals(EXACT) || modifier.equals(CONTAINS)){
                searchCondition.setModifier(modifier);
            } else {
                throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for type of 'string'.");
            }
        }

        searchCondition.setFhirSearchParameter(searchParameter);
        searchCondition.setPath(searchParameter.getPath());
        searchCondition.setValue(value);

        return searchCondition;
    }

    public static List<FhirSearchCondition> generateSearchConditionForToken(FhirSearchParameter searchParameter,
                                                                            String modifier,
                                                                            String value) throws FhirSearchException{
        List<FhirSearchCondition> searchConditions = new ArrayList<>();

        FhirSearchCondition fhirSearchCondition = new FhirSearchCondition();
        fhirSearchCondition.setFhirSearchParameter(searchParameter);

        if (modifier != null) {
            if (modifier.equals(MISSING) || modifier.equals(TEXT) || modifier.equals(NOT)
                    || modifier.equals(NOT_IN) || modifier.equals(BELOW) || modifier.equals(ABOVE)
                    || modifier.equals(IN) || modifier.equals(OF_TYPE)){

                fhirSearchCondition.setModifier(modifier);
            } else {
                throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for type of 'token'.");
             }
        }

        String dataType = searchParameter.getDataType();
        String path = searchParameter.getPath();
        String path4Sys = null;

        String[] strings = StringUtil.splitString(value, "\\|");

        if(modifier != null && modifier.equals(TEXT)){
            //process :text modifier, only support Coding.display, CodeableConcept.coding.display and Identifier.type.text
            if (dataType.equals("Coding")) {
                path = path + ".display";
            } else if (dataType.equals("CodeableConcept")) {
                path = path + ".coding.display";
            } else if (dataType.equals("Identifier")) {
                path = path + ".type.text";
            } else if (dataType.equals("ContactPoint")) {
                path = path + ".value";
            }
        } else {
            if (dataType.equals("Coding")) {
                path4Sys = path + ".system";
                path = path + ".code";
            } else if (dataType.equals("CodeableConcept")) {
                path4Sys = path + ".coding.system";
                path = path + ".coding.code";
            } else if (dataType.equals("Identifier")) {
                path4Sys = path + ".system";
                path = path + ".value";
            } else if (dataType.equals("ContactPoint")) {
                path = path + ".value";
            } else {
                path4Sys = null;
            }
        }


        String systemValue = null;
        if (path4Sys != null && strings.length>1){
            value = strings[strings.length-1];
            systemValue = strings[0];

            FhirSearchCondition searchCondition = new FhirSearchCondition();
            //Represent the pattern of '|[value]' which means no system property.
            if (systemValue.equals("")){
                searchCondition.setPath(path4Sys);
                searchCondition.setValue("true");
                searchCondition.setModifier(MISSING);
            } else {
                searchCondition.setPath(path4Sys);
                searchCondition.setValue(systemValue);
                searchCondition.setModifier(modifier);
            }

            searchConditions.add(searchCondition);
        }

        if (!value.equals("")) {
            fhirSearchCondition.setPath(path);
            fhirSearchCondition.setValue(value);

            searchConditions.add(fhirSearchCondition);
        }
        return searchConditions;
    }

    public static FhirSearchCondition generateSearchConditionForReference(FhirSearchParameter searchParameter,
                                                                          String modifier,
                                                                          FhirSearchParameter chainedParameter,
                                                                          String value) throws FhirSearchException {
        FhirSearchCondition fhirSearchCondition = new FhirSearchCondition();
        fhirSearchCondition.setFhirSearchParameter(searchParameter);

        String path = searchParameter.getPath() + ".reference";
        fhirSearchCondition.setPath(path);

        if (chainedParameter != null) {
            List<FhirSearchCondition> chainedSearchConditions = buildSearchCondition(chainedParameter, null, modifier, value);
            fhirSearchCondition.setChainedCondition(chainedSearchConditions);
        } else {
            List<String> referenceTypes = searchParameter.getReferenceTypes();

            if (modifier != null) {
                //For reference, only accept missing, above, below and [type] as modifier
                if (modifier.equals(MISSING) || modifier.equals(ABOVE) || modifier.equals(BELOW) || referenceTypes.contains(modifier)) {
                    fhirSearchCondition.setModifier(modifier);
                    //TODO process :identifier
                } else {
                    throw new FhirSearchException(400, "The modifier '" + modifier + "' is not valid for type of 'reference'.");
                }

                if (modifier.equals(MISSING)) {
                    return createFhirSearchConditionForMissing(searchParameter, modifier, value);
                }
            }

            //当一个reference指向多个不同类型的资源引用时， 应该拒绝parameter=[id]的查询，而应该使用parameter=[type]/[id]或者parameter:[type]=id
            if (value.indexOf("/") == -1) {
                if (referenceTypes.size() > 1) {
                    if (modifier == null) {
                        throw new FhirSearchException(403, "This reference refer to multiple resource types. Without resource type in the search is not allowed.");
                    } else {
                        //Turn parameter:[type]=[id] to parameter=[type]/[id]
                        value = modifier + "/" + value;
                    }
                } else {
                    //Turn parameter=[id] to parameter=[type]/[id]
                    value = referenceTypes.get(0) + "/" + value;
                }
            }

            fhirSearchCondition.setValue(value);
        }

        return fhirSearchCondition;
    }

    public static List<FhirSearchCondition> generateSearchConditionForNumber(FhirSearchParameter searchParameter,
                                                                       String modifier,
                                                                       String value) throws FhirSearchException{

        List<FhirSearchCondition> fhirSearchConditions = new ArrayList<>();

        if (modifier != null){
            //For number, only accept missing as modifier
            if (modifier.equals(MISSING)){
                fhirSearchConditions.add(createFhirSearchConditionForMissing(searchParameter, modifier, value));
                return fhirSearchConditions;
            } else {
                throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for search parameter type of 'number'");
            }

        }

        String prefix = null;
        if (value.startsWith("eq") || value.startsWith("ne") || value.startsWith("gt") ||
                value.startsWith("lt") || value.startsWith("sa") || value.startsWith("eb") || value.startsWith("ap")) {
            prefix = value.substring(0, 2);
            value = value.substring(2, value.length());
        }


        String dataType = searchParameter.getDataType();
        if (dataType.equals("decimal") && (prefix == null || prefix.equals("eq") || prefix.equals("ne"))){
            /*BigDecimal bigDecimal = new BigDecimal(value);
            int scale = bigDecimal.scale();
            //int precision = bigDecimal.precision();

            BigDecimal deviation = new BigDecimal("1e"+new BigDecimal(scale).multiply(new BigDecimal("-1"))).divide(DIVIDE);
             */
            BigDecimal up = getRangeUp(value);
            BigDecimal low = getRangeLow(value);

            FhirSearchCondition conditionUp = new FhirSearchCondition(
                    searchParameter.getPath(),
                    modifier,
                    "le",
                    up.toPlainString(),
                    searchParameter);

            FhirSearchCondition conditionLow = new FhirSearchCondition(
                    searchParameter.getPath(),
                    modifier,
                    "ge",
                    low.toPlainString(),
                    searchParameter);

            fhirSearchConditions.add(conditionLow);
            fhirSearchConditions.add(conditionUp);
        } else {
            FhirSearchCondition condition = new FhirSearchCondition(
                    searchParameter.getPath(),
                    modifier,
                    prefix,
                    value,
                    searchParameter);
        }

        return fhirSearchConditions;

    }

    public static List<FhirSearchCondition> generateSearchConditionForDate(FhirSearchParameter searchParameter, String modifier, String value) throws FhirSearchException{
        List<FhirSearchCondition> fhirSearchConditions = new ArrayList<>();

        //For date, only accept missing as modifier
        if (modifier!=null){
            if(modifier.equals(MISSING)) {
                fhirSearchConditions.add(createFhirSearchConditionForMissing(searchParameter, modifier, value));
                return fhirSearchConditions;
            } else {
                throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for search parameter type of 'date'");
            }
        }

        String[] strings = extractPrefixFromValue(value);
        String prefix = strings[0];
        value = strings[1];

        String start = null;
        String end = null;

        String dataType = searchParameter.getDataType();
        try {
            if (dataType.equals("date")) {
                if (value.length() == 4) {
                    Year year = Year.parse(value, DateTimeFormatter.ofPattern(YEARFORMATER));
                    start = year.atDay(1).toString();
                    end = value + "-12-31";
                } else if (value.length() == 7) {
                    YearMonth yearMonth = YearMonth.parse(value, DateTimeFormatter.ofPattern(YEARMONTHFORMATER));
                    start = yearMonth.atDay(1).format(DateTimeFormatter.ofPattern(DATEFORMATER));
                    end = yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern(DATEFORMATER));
                } else {
                    LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATEFORMATER));
                    start = localDate.format(DateTimeFormatter.ofPattern(DATEFORMATER));
                }
            } else if (dataType.equals("dateTime")) {
                //start = fullFormat.format(date);
                if (value.length() == 4) {
                    Year year = Year.parse(value, DateTimeFormatter.ofPattern(YEARFORMATER));
                    start = year.atDay(1).atStartOfDay().toString();
                    end = value + "-12-31T23:59:59";
                } else if (value.length() == 7) {
                    YearMonth yearMonth = YearMonth.parse(value, DateTimeFormatter.ofPattern(YEARMONTHFORMATER));
                    start = yearMonth.atDay(1).atStartOfDay().toString();
                    end = yearMonth.atEndOfMonth().atTime(23, 59, 59).toString();
                } else if (value.length() == 10) {
                    LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATEFORMATER));
                    start = localDate.atStartOfDay().toString();
                    end = localDate.atTime(23, 59, 59).toString();
                } else {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, DateTimeFormatter.ofPattern(DATETIMEFORMAT));
                    start = zonedDateTime.format(DateTimeFormatter.ofPattern(DATETIMEFORMAT));
                }
            }
        } catch (DateTimeParseException dtpe){
            throw new FhirSearchException(403, dtpe.getMessage());
        }

        if ((prefix == null || prefix.equals("eq") || prefix.equals("ne")) && end != null) {
            FhirSearchCondition conditionStart = new FhirSearchCondition();
            conditionStart.setPrefix("ge");
            conditionStart.setFhirSearchParameter(searchParameter);
            conditionStart.setPath(searchParameter.getPath());
            conditionStart.setValue(start);

            FhirSearchCondition conditionEnd = new FhirSearchCondition();
            conditionEnd.setPrefix("le");
            conditionEnd.setFhirSearchParameter(searchParameter);
            conditionEnd.setPath(searchParameter.getPath());
            conditionEnd.setValue(end);

            if (prefix != null && prefix.equals("ne")) {
                conditionStart.setModifier(NOT);
                conditionEnd.setModifier(NOT);
            }

            fhirSearchConditions.add(conditionStart);
            fhirSearchConditions.add(conditionEnd);
        } else {
            FhirSearchCondition conditionStart = new FhirSearchCondition();
            conditionStart.setPrefix(prefix);
            conditionStart.setFhirSearchParameter(searchParameter);
            conditionStart.setPath(searchParameter.getPath());

            if (end == null || prefix.equals("ge") || prefix.equals("gt") || prefix.equals("sa")) {
                conditionStart.setValue(start);
            } else if (prefix.equals("le") || prefix.equals("eb") || prefix.equals("lt")){
                    conditionStart.setValue(end);
            } else{
                //TODO For prefix 'ap'
            }

            fhirSearchConditions.add(conditionStart);
        }

        return fhirSearchConditions;
    }

    public static List<FhirSearchCondition> generateSearchConditionForQuantity(FhirSearchParameter searchParameter, String modifier, String value) throws FhirSearchException{
        List<FhirSearchCondition> fhirSearchConditions = new ArrayList<>();
        String numberPath = searchParameter.getPath().concat(".value");
        String systemPath = searchParameter.getPath().concat(".system");
        String codePath = searchParameter.getPath().concat(".code");


        if (modifier != null && modifier.equals(MISSING)) {
            //For number, only accept missing as modifier
            fhirSearchConditions.add(createFhirSearchConditionForMissing(searchParameter, modifier, value));
            return fhirSearchConditions;
        } else if (modifier != null){
            throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for search parameter type of 'quantity'");
        }

        String prefix = null;
        if (value.startsWith("eq") || value.startsWith("ne") || value.startsWith("gt") ||
                value.startsWith("lt") || value.startsWith("sa") || value.startsWith("eb") || value.startsWith("ap")) {
            prefix = value.substring(0, 2);
            value = value.substring(2, value.length());
        }

        String[] values = StringUtil.splitString(value, "\\|");

        if (values.length != 1 && values.length !=3){
            throw new FhirSearchException(403, "The value "+value +" for quantity is not valid.");
        }

        String number = values[0];
        String system = values.length!=1 ? values[1] : null ;
        String unitCode = values.length!=1 ? values[2] : null;

        if (prefix == null || prefix.equals("eq") || prefix.equals("ne")){
            BigDecimal upNumber = getRangeUp(number);
            BigDecimal lowNumber = getRangeLow(number);

            FhirSearchCondition conditionUp = new FhirSearchCondition(
                    numberPath,
                    null,
                    "le",
                    upNumber.toPlainString(),
                    searchParameter);

            FhirSearchCondition conditionLow = new FhirSearchCondition(
                    numberPath,
                    null,
                    "ge",
                    lowNumber.toPlainString(),
                    searchParameter);

            fhirSearchConditions.add(conditionLow);
            fhirSearchConditions.add(conditionUp);
        }

        if (system != null){
            FhirSearchCondition systemCondition = new FhirSearchCondition(
                    systemPath,
                    null,
                    null,
                    system,
                    searchParameter);

            fhirSearchConditions.add(systemCondition);
        }

        if (unitCode != null){
            FhirSearchCondition codeCondition = new FhirSearchCondition(
                    codePath,
                    null,
                    null,
                    unitCode,
                    searchParameter);

            fhirSearchConditions.add(codeCondition);
        }

        return fhirSearchConditions;
    }

    public static FhirSearchCondition generateSearchConditionForUri(FhirSearchParameter searchParameter,
                                                                    String modifier,
                                                                    String value) throws FhirSearchException{
        if (modifier != null && !(modifier.equals(MISSING) || modifier.equals(ABOVE) || modifier.equals(BELOW))){
            throw new FhirSearchException(403, "The modifier '" + modifier + "' is not valid for type of 'uri'.");
        }

        return new FhirSearchCondition(searchParameter.getPath(),
                modifier, null, value, searchParameter);
    }

    public static FhirSearchCondition generateSearchConditionForComposite() throws FhirSearchException {
        //TODO support composite
        FhirSearchCondition fhirSearchCondition = new FhirSearchCondition();

        return fhirSearchCondition;
    }

    public static List<FhirSearchCondition> generateSearchConditionsFromQueryParams(HashMap<String, HashMap<String, FhirSearchParameter>>  searchParametersDef, String resourceType, String httpQueryParams, Charset charset) throws FhirSearchException {
        List<FhirSearchCondition> searchConditions = new ArrayList<>();

        HashMap<String, FhirSearchParameter> searchParameters = searchParametersDef.get(resourceType);
        HashMap<String, FhirSearchParameter> commonSearchParas = searchParametersDef.get("Common");

        if (httpQueryParams != null) {
            List<NameValuePair> nameValuePairList = URLEncodedUtils.parse(httpQueryParams, charset);
            Iterator<NameValuePair> itera = nameValuePairList.iterator();

            while (itera.hasNext()) {
                NameValuePair nameValuePair = itera.next();
                String name = nameValuePair.getName();
                String value = nameValuePair.getValue();

                //For all type :missing
                //for string, :exact, :contains,
                //for token, :text, :in, :below, :above and :not-in :of-type :not
                //for reference :type
                //for uri, :below, :above
                String modifier = null;
                String chainParameter = null;
                //extract modifier from the name if it is presented. The pattern is name:modifier
                if (name.indexOf(":") != -1) {
                    String[] strings = StringUtil.splitString(name, ":");
                    name = strings[0];
                    modifier = strings[1];
                }

                if (name.indexOf(".") != -1) {
                    String[] strings = StringUtil.splitString(name, "\\.");
                    name = strings[0];
                    chainParameter = strings[1];
                }

                if ((!searchParameters.containsKey(name) && !commonSearchParas.containsKey(name)) ||
                        name.equals("_format") || name.equals("_pretty") || name.equals("_summary") || name.equals("_elements")) {
                    //ignore unsupported or unknown search parameters. Refer to http://hl7.org/fhir/search.html
                    //TODO, add code to handle client Prefer header, Prefer: handling=strict or Prefer: handling=lenient
                    continue;
                    //throw new FhirSearchException(400, "The parameter with name '"+name+"' is not supported by the Resource Type of "+resourceType);
                }

                //Get the definition of search parameter by specified name.
                FhirSearchParameter fhirSearchParameter = searchParameters.get(name);
                FhirSearchParameter chainedParameter = null;
                FhirSearchParameterType type = fhirSearchParameter.getType();

                if (chainParameter != null && !type.equals(FhirSearchParameterType.REFERENCE)){
                    throw new FhirSearchException(403, "链式查询只能应用在Reference类型的查询参数");
                } else {
                    List<String> referenceTypes = fhirSearchParameter.getReferenceTypes();
                    if (chainParameter != null && referenceTypes.size() > 1) {
                        throw new FhirSearchException(403, "当进行Chained查询的时候，reference必须指定参考的资源类型");
                    }

                    HashMap<String, FhirSearchParameter> chainedSearchParameters = searchParametersDef.get(referenceTypes.get(0));
                    if (!chainedSearchParameters.containsKey(chainParameter)) {
                        throw new FhirSearchException(403, "Chained的查询参数非法");
                    }

                    chainedParameter = chainedSearchParameters.get(chainParameter);
                }

                searchConditions.addAll(buildSearchCondition(fhirSearchParameter, chainedParameter, modifier, value));

                //TODO Process return search parameters
                //TODO _has, _list, _type need to be supported
            }
        }
        return searchConditions;
    }

    public static List<FhirSearchCondition> buildSearchCondition(FhirSearchParameter fhirSearchParameter, FhirSearchParameter chainedParameter, String modifier, String value) throws FhirSearchException {
        List<FhirSearchCondition> fhirSearchConditions = new ArrayList<>();

        FhirSearchParameterType type = fhirSearchParameter.getType();

        switch (type) {
            case STRING:
                fhirSearchConditions.add(generateSearchConditionForString(fhirSearchParameter, modifier, value));
                break;
            case TOKEN:
                fhirSearchConditions.addAll(FhirSearchUtil.generateSearchConditionForToken(fhirSearchParameter, modifier, value));
                break;
            case REFERENCE:
                fhirSearchConditions.add(FhirSearchUtil.generateSearchConditionForReference(fhirSearchParameter, modifier, chainedParameter, value));
                break;
            case NUMBER:
                fhirSearchConditions.addAll(FhirSearchUtil.generateSearchConditionForNumber(fhirSearchParameter, modifier, value));
                break;
            case DATE:
                fhirSearchConditions.addAll(FhirSearchUtil.generateSearchConditionForDate(fhirSearchParameter, modifier, value));
                break;
            case QUANTITY:
                fhirSearchConditions.addAll(FhirSearchUtil.generateSearchConditionForQuantity(fhirSearchParameter, modifier, value));
                break;
            case URI:
                fhirSearchConditions.add(FhirSearchUtil.generateSearchConditionForUri(fhirSearchParameter, modifier, value));
                break;
            case COMPOSITE:
                //TODO For Composite search parameters
                break;
            default:
                break;
        }

        String restrict = fhirSearchParameter.getRestrict();
        if (restrict != null) {
            String[] strings = restrict.split("=");
            String p = fhirSearchParameter.getPath() + "." + strings[0];
            String v = strings[1];

            FhirSearchCondition searchCondition = new FhirSearchCondition();
            searchCondition.setPath(p);
            searchCondition.setValue(v);

            fhirSearchConditions.add(searchCondition);
        }

        return fhirSearchConditions;
    }

    private static String[] extractPrefixFromValue(String value){
        String prefix = null;

        if (value.startsWith("eq") || value.startsWith("ne") || value.startsWith("gt") ||
                value.startsWith("lt") || value.startsWith("sa") || value.startsWith("eb") ||
                value.startsWith("ap") || value.startsWith("le") || value.startsWith("ge")) {
            prefix = value.substring(0, 2);
            value = value.substring(2, value.length());
        }

        return new String[]{prefix, value};
    }

    private static FhirSearchCondition createFhirSearchConditionForMissing(FhirSearchParameter searchParameter, String modifier, String value){
        FhirSearchCondition condition = new FhirSearchCondition();
        condition.setFhirSearchParameter(searchParameter);
        condition.setModifier(modifier);
        condition.setPath(searchParameter.getPath());
        condition.setValue(value);

        return condition;
    }

    private static String getLastTimeFromMonth(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIMEFORMAT);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return dateFormat.format(cal.getTime());
    }

    private static String getLastTimeFromDay(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIMEFORMAT);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return dateFormat.format(cal.getTime());
    }

    private static String getLastDayFromMonth(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return dateFormat.format(cal.getTime());
    }

    private static BigDecimal getRangeUp(String value){
        BigDecimal bigDecimal = new BigDecimal(value);
        int scale = bigDecimal.scale();
        //int precision = bigDecimal.precision();

        BigDecimal deviation = new BigDecimal("1e"+new BigDecimal(scale).multiply(new BigDecimal("-1"))).divide(DIVIDE);

        return bigDecimal.add(deviation);
    }

    private static BigDecimal getRangeLow(String value){
        BigDecimal bigDecimal = new BigDecimal(value);
        int scale = bigDecimal.scale();
        //int precision = bigDecimal.precision();

        BigDecimal deviation = new BigDecimal("1e"+new BigDecimal(scale).multiply(new BigDecimal("-1"))).divide(DIVIDE);

        return bigDecimal.subtract(deviation);
    }
}
