package com.hugelol.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

import com.hugelol.http.HTTPClient;
import com.hugelol.model.Hugelol;

public abstract class HugelolParser {

    public static Hugelol doParse(String response, Context context) throws Exception{
        List<String> lolPost = parseResponse(response);
        Hugelol hugelol = new Hugelol();
        Integer id = Integer.parseInt(lolPost.get(0));
        hugelol.setId(id);
        Calendar date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        date.setTime(new Date(Long.parseLong(lolPost.get(1))));
        hugelol.setDate(date);
        hugelol.setUserID(Integer.parseInt(lolPost.get(2)));
        hugelol.setUserName(lolPost.get(3).substring(1, lolPost.get(3).length() - 1));
        hugelol.setType(Integer.parseInt(lolPost.get(4)));
        String lolURL = lolPost.get(5).substring(1, lolPost.get(5).length() - 1);
        hugelol.setUrl(lolURL);
        HTTPClient httpClient = new HTTPClient();
        byte[] lolImage = httpClient.getResponseAsByteArray(lolURL);
        hugelol.setImage(lolImage);
        hugelol.setTitle(escapeQuotes(lolPost.get(6).substring(1, lolPost.get(6).length() - 1)));
        if(lolPost.get(7).matches("\\d+")){
            hugelol.setLikes(Integer.parseInt(lolPost.get(7)));
        }else{
            hugelol.setLikes(0);
        }
        if(lolPost.get(8).matches("\\d+")){
            hugelol.setDislikes(Integer.parseInt(lolPost.get(8)));
        }else{
            hugelol.setDislikes(0);
        }
        return hugelol;
    }
    
    private static String escapeQuotes(String title){
        return title.replaceAll("&quot;", String.valueOf('"'));
    }
    
    private static List<String> parseResponse(String result){
        List<String> lolPost = new ArrayList<String>();
        int start = 1;
        boolean quote = false;
        for(int i = 0; i < result.length(); i++){
            char c = result.charAt(i);
            if(c == ',' && !quote){
                lolPost.add(result.substring(start, i));
                start = i + 1;
                if(result.charAt(i+1) == '"'){
                    quote = true;
                }
            }else if(c == ',' && result.charAt(i - 1) == '"'){
                lolPost.add(result.substring(start, i));
                start = i + 1;
                quote = false;
                if(result.charAt(i+1) == '"'){
                    quote = true;
                }
            }
        }
        return lolPost;
    }
}
