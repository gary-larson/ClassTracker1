package com.antonioramos.classtracker1;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Location currentLocation;
    private CameraPosition cameraPosition;
    CurrentLocationActivity currentLocationActivity = new CurrentLocationActivity();

    private String name;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
        longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
        latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button b =(Button)findViewById(R.id.go_button);
        b.setOnClickListener(this);

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
        goToLocationZoom(latitude, longitude,15, name);
    }
    private void goToLocationZoom(double lat, double lng, float zoom,String locationName){
        LatLng lat_lng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lat_lng, zoom);
        mMap.addMarker(new MarkerOptions().position(lat_lng).title(locationName));
        mMap.moveCamera(update);
    }
    private void geoLocateHelper()throws IOException{
        EditText et =(EditText)findViewById(R.id.location_editText);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<android.location.Address> list = gc.getFromLocationName(location,1);
        android.location.Address address =list.get(0);

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lat,lng,15f,location);


    }

    @Override
    public void onClick(View view) {
        try {
            geoLocateHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
