package com.fs.hc.fhir.core.util;


import org.apache.commons.text.StringEscapeUtils;

public class StringUtil {
    public static String[] splitString(String value, String split){
        String[] values = value.split(split);

        String sp = StringEscapeUtils.unescapeJava(split);

        if (value.endsWith(sp)) {
            values = concat(values, new String[]{""});
        }

        if (value.startsWith(sp)) {
            values = concat(new String[]{""}, values);
        }

        return values;
    }

    public static String[] concat(String[] array1, String[] array2){
        if (array1 == null || array1.length == 0){
            return array2;
        }

        if (array2 == null || array2.length == 0){
            return array1;
        }

        String[] newArray = new String[array1.length+array2.length];

        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);

        return newArray;
    }
}
