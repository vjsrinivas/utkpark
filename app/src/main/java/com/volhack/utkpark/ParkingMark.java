package com.volhack.utkpark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.*;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;



//JSONObject parking = (JSONObject) obj;

//JSONArray park = (JSONArray) park.get("parking");

class ParkingMark{
    private String lot;
    private float lat;
    private float lng;

    //public JSONArray park = (JSONArray) parking.get("parking");
    //private List<String> lot_names = new ArrayList<>();

    public void setName(String name){
        lot = name;
    }

    public void setLat(float latIn){
        lat = latIn;
    }

    public void setLng(float lngIn){
        lng = lngIn;
    }

    public String getName(){
        return lot;
    }

    public float getLat(){
        return lat;
    }

    public float getLng(){
        return lng;
    }
}

class ParkingLotData{

    public List<ParkingMark> lots = new ArrayList<>();
    private Object obj;
    public JSONObject parking;
    public JSONArray park;

    public void ParkingLotData(){

        try{
            JSONParser parser = new JSONParser();
            obj = parser.parse(new FileReader(
                    "parkingcords.json"));
            parking = (JSONObject) obj;
            park = (JSONArray) parking.get("park");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 0; i < parking.length(); i++) {
                ParkingMark temp_lot = new ParkingMark();
                JSONObject temp_json = (JSONObject) park.get(i);
                temp_lot.setName((String) temp_json.get("lot"));
                temp_lot.setLat((int) temp_json.get("lat"));
                temp_lot.setLng((int) temp_json.get("lng"));
                lots.add(temp_lot);
            }
        }
        catch(Exception e ){
            //
        }
    }
}