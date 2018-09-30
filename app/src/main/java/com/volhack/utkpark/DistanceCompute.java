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
public class DistanceCompute extends AsyncTask<TaskParams, Void, double[]> {

    protected double[] doInBackground(TaskParams... taskData){
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json";
        double[] coor = {taskData[0].pos[0],taskData[0].pos[1],0,0};
        //run loop and shit through lots
        double shortestDistance;
        JSONObject shortestDistanceIndex;
        JSONParser parser = new JSONParser();

        try {
            //run first one to initalize:
            String params[] = {"units=imperial", "origins=" + taskData[0].pos[0] + "," + taskData[0].pos[1], "destinations="+taskData[0].lotData.lots.get(0).getLat()+","+taskData[0].lotData.lots.get(0).getLng(), "key=AIzaSyA571UCwHR860O78oW_xnGHri6lOhXzXns"};
            //str_base contains url_base and params
            String raw_json;
            raw_json = DistanceUtil.convertStreamToString(DistanceUtil.requestMatrixDistance(url, params));
            try {
                JSONObject json = (JSONObject) parser.parse(raw_json);
                JSONArray temp = (JSONArray) json.get("rows");
                JSONObject temp2 =  (JSONObject) temp.get(0);
                temp = (JSONArray) temp2.get("elements");
                temp2 = (JSONObject) temp.get(0);
                temp2 =  (JSONObject) temp2.get("duration");
                String time = (String) temp2.get("text");
                String newer="";
                for (char ch: time.toCharArray()) {
                    if(ch == ' '){
                        System.out.println("uhoh");
                        break;
                    }
                    else{
                        newer += ch;
                    }
                }
                shortestDistanceIndex = json;
                shortestDistance = Double.parseDouble(newer);
                coor[2] = taskData[0].lotData.lots.get(0).getLat();
                coor[3] = taskData[0].lotData.lots.get(0).getLng();

            } catch (Exception e) {
                System.out.println("rip!");
                return new double[]{0,0,0,0};
            }
        } catch (IOException e) {
            System.out.println("rip!");
            return new double[]{0,0,0,0};
        }

        //System.out.println("hi!"+taskData[0].lotData.lots.size());
        //System.out.println(taskData[0].lotData.lots.get(i).getName());


        for(int i = 0; i < taskData[0].lotData.lots.size(); i++) {
            String params[] = {"units=imperial", "origins=" + taskData[0].pos[0] + "," + taskData[0].pos[1], "destinations=" + taskData[0].lotData.lots.get(i).getLat() + "," + taskData[0].lotData.lots.get(i).getLng(), "key=AIzaSyA571UCwHR860O78oW_xnGHri6lOhXzXns"};
            //str_base contains url_base and params
            String raw_json;
            //System.out.println("hi!");
            try {
                raw_json = DistanceUtil.convertStreamToString(DistanceUtil.requestMatrixDistance(url, params));
            } catch (IOException e) {
                //System.out.println("hi!");
                return new double[]{0,0,0,0};
                //raw_json = "";
            }


            try {
                JSONObject json = (JSONObject) parser.parse(raw_json);
                System.out.println("suck!");
                JSONArray temp = (JSONArray) json.get("rows");
                JSONObject temp2 =  (JSONObject) temp.get(0);
                temp = (JSONArray) temp2.get("elements");
                temp2 = (JSONObject) temp.get(0);
                temp2 =  (JSONObject) temp2.get("duration");
                String time = (String) temp2.get("text");
                String newer="";
                for (char ch: time.toCharArray()) {
                    if(ch == ' '){
                        System.out.println("uhoh");
                        break;
                    }
                    else{
                        newer += ch;
                    }
                }

                if(Double.parseDouble(newer) > shortestDistance){
                    shortestDistance = Double.parseDouble(newer);
                    shortestDistanceIndex = json;
                    coor[2] = taskData[i].lotData.lots.get(0).getLat();
                    coor[3] = taskData[i].lotData.lots.get(0).getLng();
                }
            } catch (Exception e) {
                return new double[]{0,0,0,0};
            }
        }
        System.out.println("the resultV");
        for(int i=0; i<coor.length; i++) {
            System.out.println(coor[i]);
        }
        return coor;

    }

    protected double[] onPostExecute(double[]... result) {
        new DirectionCalculations().execute(result[0]);
        return result[0];
    }
}
