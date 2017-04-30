package com.antonioramos.classtracker1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private double defaultLat =36.534342;
    private double defaultLong=-87.354328;
    private String defaultName = "APSU";
    private float defualtZoom = 16.5f;


    public static final String DATA_FILENAME = "locations.loc";

    private String name;
    private double longitude;
    private double latitude;
    private MarkerOptions options = new MarkerOptions();

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

    }

    public void readData(GoogleMap mMap) {
        Log.i("INFO", "---------- ************************************Read ");
        try {
            // Log.i("INFO", "---------- Entered Read");
           // FileInputStream fis = openFileInput(DATA_FILENAME);
            InputStreamReader isr = new InputStreamReader(getApplicationContext().getAssets().open(DATA_FILENAME));
            Scanner scanner = new Scanner(isr).useDelimiter(",");

            // Log.i("INFO", "---------- Read Setup Done");
            while (scanner.hasNext()) {
                // Log.i("INFO", "---------- Read has DATA");

                String s = scanner.nextLine();
                String[] tokens = s.split(",");
                name = tokens[0];
                longitude = Double.parseDouble(tokens[1]);
                latitude = Double.parseDouble(tokens[2]);

                LatLng lat_lng2 = new LatLng(latitude, longitude);
                options.position(lat_lng2);
                options.title(name);
                mMap.addMarker(options);
            }

           LatLng lat_lng = new LatLng(defaultLat, defaultLong);
          options.position(lat_lng);
            options.title(defaultName);
            mMap.addMarker(options);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lat_lng, defualtZoom);
            mMap.moveCamera(update);
            scanner.close();
            mMap.moveCamera(update);
        } catch (FileNotFoundException e) {
            Log.i("INFO", "---------- ************************************Read Exception");
            // ok if file does not exist
        } catch (IOException e) {
            e.printStackTrace();
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
        readData(googleMap);
        mMap.setOnInfoWindowClickListener(this);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String s = marker.getPosition().toString();
        try {
            String loc = getIntent().getExtras().getString(s);
            String[] tokens = loc.split(",");

            longitude = Double.parseDouble(tokens[0]);
            latitude = Double.parseDouble(tokens[1]);
            name = marker.getTitle();
        }catch (NullPointerException e){
            Log.i("null ", " ****************");

        }

        Intent intent = new Intent(getApplicationContext(), Navigation.class);
        intent.putExtra("name",name);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
    }
}
