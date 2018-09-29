package javaapplication1;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


private String lot;

private float lat;

private float lng;


public String getGarage(String lot) {
        this.lot = lot;
    return lot;
}

public float getLat(float lat) {
    this.lat = lat;
    return lat;
}

public float getLng(float lng) {
    this.lng = lng;
    return lng;
}

