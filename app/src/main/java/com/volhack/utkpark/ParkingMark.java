package com.volhack.utkpark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;



//JSONObject parking = (JSONObject) obj;

//JSONArray park = (JSONArray) park.get("parking");

class ParkingMark{
    private String lot;
    private double lat;
    private double lng;

    //public JSONArray park = (JSONArray) parking.get("parking");
    //private List<String> lot_names = new ArrayList<>();

    public void setName(String name){
        lot = name;
    }

    public void setLat(double latIn){
        lat = latIn;
    }

    public void setLng(double lngIn){
        lng = lngIn;
    }

    public String getName(){
        return lot;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }
}

class ParkingLotData{

    public List<ParkingMark> lots = new ArrayList<>();
    private Object obj;
    public JSONObject parking;
    public JSONArray park;

    ParkingLotData(){

        try{
            JSONParser parser = new JSONParser();
            obj = parser.parse(Desperate.JSONCRY);
            parking = (JSONObject) obj;
            park = (JSONArray) parking.get("parking");
            //System.out.println("test!"+parking.get("parking"));
        }
        catch(Exception e){
            System.out.println("bleh!@!");
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 0; i < park.size(); i++) {
                ParkingMark temp_lot = new ParkingMark();
                JSONObject temp_json = (JSONObject) park.get(i);
                //System.out.println("test!"+temp_json);
                temp_lot.setName((String) temp_json.get("lot"));
                temp_lot.setLat(Double.parseDouble( (String) temp_json.get("lat")));
                temp_lot.setLng(Double.parseDouble((String) temp_json.get("lng")));
                lots.add(temp_lot);
            }
        }
        catch(Exception e ){
            //
            System.out.println(e.getMessage());
        }
    }
}