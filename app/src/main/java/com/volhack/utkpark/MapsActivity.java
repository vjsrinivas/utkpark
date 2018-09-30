package com.volhack.utkpark;

import android.graphics.Path;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;

import android.support.annotation.NonNull;
import java.util.*;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import org.json.simple.JSONObject;

class TaskParams{
    ParkingLotData lotData;
    double pos[] = {0,0,0,0};

    TaskParams(ParkingLotData lotData, double pos[]){
        this.lotData = lotData;
        this.pos = pos;
    }

}

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private boolean mRequestingLocationUpdates = true;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private ParkingLotData storedData;
    public static String polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    private void startLocationUpdates() {
        if(mLocationCallback != null || mLocationCallback != null) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        LatLngBounds UTK = new LatLngBounds(
                new LatLng(35.934409,  -83.957216), new LatLng( 35.966447, -83.921553 ));
        // Constrain the camera target to the UTK bounds.
        mMap.setLatLngBoundsForCameraTarget(UTK);

        //Adds the marker according to the parking garage data
        List<Marker> marks = new ArrayList<>();
        storedData = new ParkingLotData();

        for(int k=0; k < storedData.lots.size(); k++){
            Marker temp_mark;
            temp_mark = mMap.addMarker(new MarkerOptions()
            .position(new LatLng(storedData.lots.get(k).getLat(), storedData.lots.get(k).getLng()))
            .title(storedData.lots.get(k).getName()));
            marks.add(temp_mark);
            temp_mark.setTag(0);
        }        

        // Set a listener for marker click.
        //mMap.setOnMarkerClickListener(this);

        enableMyLocation();
        if(!mPermissionDenied) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double coor[] = {location.getLatitude(), location.getLongitude()};
                                LatLng initial_location = new LatLng(coor[0], coor[1]);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial_location, 17.0f));
                                TaskParams taspar = new TaskParams(storedData, coor);
                                new DistanceCompute().execute(taspar);

                            }
                        }
                    });

            //String params_1[] = {""}
            addPolyLine("udnzEnrf_ONzAb@vDJbA`@nBg@Te@{Bg@iCEs@o@aESaBMLsCxAwAr@|@fD`Bw@?KES");
        }
    }

    /* //Called when the user clicks a marker.
     @Override
     public boolean onMarkerClick(final Marker marker) {
         // Retrieve the data from the marker.
        Change the variable type to what you want to return 
        Integer clickCount = (Integer) marker.getTag();
    } */

    //EDITED
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, MY_LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_LOCATION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void addPolyLine(String polypath){
        List<LatLng> decodedPath = PolyUtil.decode(polypath);
        PolylineOptions polyop = new PolylineOptions().addAll(decodedPath);
        polyop.color(0xFFFF8200);
        mMap.addPolyline(polyop);
        //mMap.clear();
    }

    public static void finalPoly(String data){
        polyline = data;
    }
}
