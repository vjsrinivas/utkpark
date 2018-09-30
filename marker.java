package javaapplication1;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

Object obj = new JSONParser().rparse(new FileReader("pakingcords.json"));

JSONObject parking = (JSONObject) obj;

JSONArray park = (JSONArray) park.get("parking");

class parkingMark{
    String lot;
    float lat;
    float lng;

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

    public List<parkingMark> lots = new ArrayList<>();

    public void ParkingLotData(){
        for(int i; i < park.size(); i++){
            parkingMark temp_lot = new parkingMark();
            temp_lot.setName(park[i].lot);
            temp_lot.setLat(park[i].lat);
            temp_lot.setLng(park[i].lng);
            lots.add(temp_lot);
        }
    }
}