package com.hugelol.parser;

public abstract class HTTPResponseParser {
    public static String[] doParse(String response){
        response = response.replace("\\", "");
        response = response.substring(2, response.length()-2);
        String[] array = response.split("],");
        array[array.length - 1] = array[array.length - 1].substring(0,  array[array.length - 1].length() - 1);
        return array;
    }
}
