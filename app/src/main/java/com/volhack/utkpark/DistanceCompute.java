package com.volhack.utkpark;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.StringWriter;
import java.io.Reader;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class DistanceCompute extends AsyncTask<String[], Void, JSONObject> {

    private static InputStream requestMatrixDistance(String[] base, String[] params){
        //base only 1 item always
        String base_url = base[0];
        for(int i=0; i < params.length; i++){
            if(i==0) {
                base_url += "?" + params[i];
            }
            else{
                base_url += "&" + params[i];
            }
        }
        System.out.println(base_url);

        URL url;
        HttpURLConnection urlConnection;
        InputStream in;

        try {
            url = new URL(base_url);
            urlConnection = (HttpURLConnection) url.openConnection();
        }
        catch(IOException e) {
            return null;
        }
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
        }
        catch(IOException e) {
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
        return in;
    }

    private static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    protected JSONObject doInBackground(String[]... str_base){
        //str_base contains url_base and params
        String raw_json;
        //System.out.println("hi!");
        try {
            raw_json = convertStreamToString(requestMatrixDistance(str_base[0], str_base[1]));
            System.out.println(raw_json);
        }
        catch(IOException e){
             //System.out.println("hi!");
             return null;
             //raw_json = "";
        }
        try {
            JSONObject json = new JSONObject(raw_json);
            return json;
        }
        catch(JSONException e) {
            return null;
        }
    }
}
