package com.example.ziku.hotshot.jsonservices;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Ziku on 2016-11-18.
 */

public class JsonHttp {

    private static final String TAG = JsonHttp.class.getSimpleName();

    public JsonHttp(){}

    public String JsonServiceCall(String urlstring){
        String response = null;
        try{
            URL url = new URL(urlstring);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            response = convertContentToString(inputStream);
        } catch (MalformedURLException ex){
            Log.d(TAG,"MalformedURLException");
        } catch (ProtocolException ex){
            Log.d(TAG,"ProtocolException");
        } catch (IOException ex){
            Log.d(TAG,"IOException");
        } catch (Exception ex){
            Log.d(TAG,"Exception");
        }
        return response;
    }

    public String convertContentToString(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try{
            while((line = reader.readLine()) != null ){
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        String lastString = stringBuilder.toString();
        lastString = "{\"list\":" + lastString + "}";
        return lastString;
    }
}