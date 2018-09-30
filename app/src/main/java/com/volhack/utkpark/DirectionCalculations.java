package com.volhack.utkpark;

import android.os.AsyncTask;

import com.google.android.gms.tasks.Task;

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

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;

public class DirectionCalculations extends AsyncTask<double[], Void, String> {
    protected String doInBackground(double[]... taskData) {
        System.out.println("sgsdgsdgsdgds");
        String polyString="";
        String url = "https://maps.googleapis.com/maps/api/directions/json";
        String params[] = {"origins=" + taskData[0] + "," + taskData[1], "destinations="+taskData[2]+","+taskData[3], "key=AIzaSyA571UCwHR860O78oW_xnGHri6lOhXzXns"};
        try {
            JSONParser parser = new JSONParser();
            String raw_json = DistanceUtil.convertStreamToString(DistanceUtil.requestMatrixDistance(url, params));
            JSONObject json = (JSONObject) parser.parse(raw_json);
            JSONArray temp = (JSONArray) json.get("routes");
            json = (JSONObject) temp.get(0);
            System.out.println("blahsfasf");
            System.out.println(json);
        }
        catch (Exception e){
            System.out.println("afaf"+e.getMessage());
        }
        return polyString;
    }

    protected String onPostExecute(String... result) {
        MapsActivity.finalPoly(result[0]);
        return result[0];
    }
}
