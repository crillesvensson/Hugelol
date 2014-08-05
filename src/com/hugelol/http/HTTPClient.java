package com.hugelol.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HTTPClient {
	public static final int STATUS_CODE_OK = 200;

	public JSONObject getResponseAsJSON(String urlString) throws Exception {
		return new JSONObject(doRequest(urlString));
	}
	
	public String getResponseAsString(String urlString) throws Exception {
		return doRequest(urlString);
	}
	
	public JSONObject getResponseAsJSON(String urlString, JSONObject json) throws Exception {
	    JSONObject serverResponse = new JSONObject(doRequest(urlString, json));
	    return serverResponse;
	}
	
	public String getResponseAsString(String urlString, JSONObject json) throws Exception {
        return doRequest(urlString, json);
    }
	
	public Bitmap getReponseAsBitmap(String urlString) throws Exception {
        return doImageRequest(urlString);
    }
	
	public byte[] getResponseAsByteArray(String urlString) throws Exception {
	    return doByteArrayRequest(urlString);
	}
	
	private byte[] doByteArrayRequest(String urlString) throws Exception {
	    URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream in = null;
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
            int statusCode = urlConnection.getResponseCode();
            
            if (statusCode == STATUS_CODE_OK) {
                in = new BufferedInputStream(
                        urlConnection.getInputStream());
                // this dynamically extends to take the bytes you read
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                // this is storage overwritten on each iteration with bytes
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                // we need to know how may bytes were read to write them to the byteBuffer
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                  byteBuffer.write(buffer, 0, len);
                }

                // and then we can return your byte array.
                return byteBuffer.toByteArray();
            }
            
            throw new Exception(urlConnection.getResponseMessage() + " - response code: " + statusCode);
        } finally {
            urlConnection.disconnect();
            if(in != null){
                in.close();
            }
        }   
	}
    
    private Bitmap doImageRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
            int statusCode = urlConnection.getResponseCode();
            
            if (statusCode == STATUS_CODE_OK) {
                in = new BufferedInputStream(
                        urlConnection.getInputStream());
                
                bitmap = BitmapFactory.decodeStream(in);

                return bitmap;
            }
            
            throw new Exception(urlConnection.getResponseMessage() + " - response code: " + statusCode);
        } finally {
            urlConnection.disconnect();
            if(in != null){
                in.close();
            }
        }
        
    }
	
	private String doRequest(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		BufferedReader reader = null;
	

		try {
		    urlConnection.setRequestMethod("GET");
		    urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == STATUS_CODE_OK) {
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				reader = new BufferedReader(new InputStreamReader(in));

				StringBuilder stringBuilder = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}

				return stringBuilder.toString();
			}
			
			throw new Exception("no server response.. response code is " + statusCode);
		} finally {
			urlConnection.disconnect();
			if(reader != null) {reader.close();}
		}

	}
	
	private String doRequest(String urlString, JSONObject json) throws Exception{
	    URL url = new URL(urlString);
	    String message = json.toString();
	    
	    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	    BufferedReader reader = null;
	    try{
    	    urlConnection.setRequestMethod("POST");
    	    urlConnection.setDoInput(true);
    	    urlConnection.setDoOutput(true);
    	    
    	    urlConnection.setFixedLengthStreamingMode(message.getBytes().length);
    	    
    	    urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    	    urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
    	    urlConnection.connect();
    	    
    	    OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
    	    os.write(message.getBytes());
    	    os.flush();
    	    
    	    int statusCode = urlConnection.getResponseCode();
            if (statusCode == STATUS_CODE_OK) {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();
            }
            
            throw new Exception("no server response.. response code is " + statusCode);     
	    }finally{
	        urlConnection.disconnect();
            if(reader != null) {reader.close();}
	    }  
	}
}
