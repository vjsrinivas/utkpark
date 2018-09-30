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

import org.json.JSONException;
import org.json.JSONObject;

class DistanceUtil{

    public static InputStream requestMatrixDistance(String base_url, String[] params){
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

    public static String convertStreamToString(InputStream inputStream) throws IOException {
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

}

//only for Distance Matrix
public class DistanceCompute extends AsyncTask<TaskParams, Void, JSONObject> {

    protected JSONObject doInBackground(TaskParams... taskData){
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json";
        //taskData[0].lotData
        //taskData[0].pos
        //run loop and shit through lots
        int shortestDistance;
        int shortestDistanceIndex = 0;


        try {
            //run first one to initalize:
            String params[] = {"units=imperial", "origins=" + taskData[0].pos[0] + "," + taskData[0].pos[1], "destinations="+taskData[1].lotData.lots.get(1).getLat()+","+taskData[1].lotData.lots.get(1).getLng(), "key=AIzaSyA571UCwHR860O78oW_xnGHri6lOhXzXns"};
            //str_base contains url_base and params
            String raw_json;
            raw_json = DistanceUtil.convertStreamToString(DistanceUtil.requestMatrixDistance(url, params));
            try {
                JSONObject json = new JSONObject(raw_json);

            } catch (JSONException e) {
                return null;
            }
        } catch (IOException e) {
            //System.out.println("hi!");
            return null;
            //raw_json = "";
        }

        //System.out.println("hi!"+taskData[0].lotData.lots.size());
        //System.out.println(taskData[0].lotData.lots.get(i).getName());



        for(int i = 1; i < taskData[0].lotData.lots.size(); i++)
        {
            String params[] = {"units=imperial", "origins=" + taskData[0].pos[0] + "," + taskData[0].pos[1], "destinations="+taskData[1].lotData.lots.get(i).getLat()+","+taskData[1].lotData.lots.get(i).getLng(), "key=AIzaSyA571UCwHR860O78oW_xnGHri6lOhXzXns"};
            //str_base contains url_base and params
            String raw_json;
            //System.out.println("hi!");
            try {
                raw_json = DistanceUtil.convertStreamToString(DistanceUtil.requestMatrixDistance(url, params));
            } catch (IOException e) {
                //System.out.println("hi!");
                return null;
                //raw_json = "";
            }


            try {
                JSONObject json = new JSONObject(raw_json);
                return json;
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    protected JSONObject onPostExecute(JSONObject... result) {
        MapsActivity.determineShortestPath(result[0]);
        return result[0];
    }
}
