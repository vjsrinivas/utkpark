package com.volhack.utkpark;

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
import org.json.JSONObject;

public class DistanceCompute {
    private static InputStream getMatrixDistanceRequest(String given_url){
        URL url;
        HttpURLConnection urlConnection;
        InputStream in;

        try {
            url = new URL(given_url);
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

    public int getMatrixDistance(String raw_json){
        return -1;
    }
}
