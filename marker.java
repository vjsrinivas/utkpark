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

    private List<String> lot_names = new ArrayList<>();

    public void setGarage(String name){
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

Iterator eachPark = park.Iterator();

for(int i; i < park.size(); i++){
    


}