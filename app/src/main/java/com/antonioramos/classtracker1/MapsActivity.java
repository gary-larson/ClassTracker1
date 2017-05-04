package com.antonioramos.classtracker1;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
    Created by Antonio Ramos
    Class will open up Map fragment activity and place desired markers on map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    public static final String DATA_FILENAME = "locations.loc";
    public static final String MY_FILENAME = "mylocations.loc";
    private ArrayList<MyLocation> myLocations;

    //default map center point and zoom
    private double defaultLat =36.534342;
    private double defaultLong=-87.354328;
    private String defaultName = "APSU";
    private float defualtZoom = 16.5f;

    //variables to hold markers data
    private String name;
    private double longitude;
    private double latitude;
    private MarkerOptions options = new MarkerOptions(); //Marker container

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

    /**
     *  Retrieve save locations from file locations.loc and add markers them on the map.
     *  -By Antonio Ramos
     */
    public void loadMarkers(String fileName) {
        Log.i("INFO", "---------- ************************************Read ");
        int pictureSet=0;
        try {
            if (fileName.equals(MY_FILENAME)) {
                FileInputStream fis = openFileInput(MY_FILENAME);

               ObjectInputStream objectInStream = new ObjectInputStream(fis);
               myLocations =  (ArrayList) objectInStream.readObject();
                fis.close();
                for(int i = 0; i < myLocations.size();i++){
                    addMarker(myLocations.get(i).getName(),myLocations.get(i).getLatitude(),
                            myLocations.get(i).getLongitude(),210.0f,i);
                }
            } else {
                InputStreamReader isr = new InputStreamReader(getApplicationContext().getAssets().open(fileName));
                Scanner scanner = new Scanner(isr).useDelimiter(",");

                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    String[] tokens = s.split(",");
                    name = tokens[0];
                    longitude = Double.parseDouble(tokens[1]);
                    latitude = Double.parseDouble(tokens[2]);

                    addMarker(name,latitude,longitude,0.0f,pictureSet);
                    pictureSet++;
                }
                scanner.close();
            }

        } catch (FileNotFoundException e) {
            Log.i("INFO", "---------- ************************************Read Exception");
            // ok if file does not exist
            //Use APSU's Latitude and Longitude as center point on map
            LatLng lat_lng = new LatLng(defaultLat, defaultLong);
            options.position(lat_lng);
            options.title(defaultName);
            mMap.addMarker(options);

        } catch (IOException e) {
            e.printStackTrace();
            //Use APSU's Latitude and Longitude as center point on map
            LatLng lat_lng = new LatLng(defaultLat, defaultLong);
            options.position(lat_lng);
            options.title(defaultName);
            mMap.addMarker(options);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Adds markers to the map red for hardcode locations blue for users save locations
     *  -By Antonio Ramos
     */
    private void addMarker(String name1, double lat, double lng,float markerColor,int pictureSet1){
        //load makers to map
        LatLng lat_lng2 = new LatLng(lat, lng);             //merges Latitude and longitude into one variable
        options.position(lat_lng2);                         //save Lat_Lng2 in a Marker option container
        options.title(name1);                                //save name in a Marker option container
        options.icon(BitmapDescriptorFactory.defaultMarker(markerColor)); //change color of marker 0.0f red, 210.0f blue
        options.snippet(Integer.toString(pictureSet1));
        mMap.addMarker(options);                            //adds marker option contents to map

        //Log.i("check", "****"+ BitmapDescriptorFactory.HUE_RED); //use to check color number of marker
    }

        /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMarkers(DATA_FILENAME);
        //Use APSU's Latitude and Longitude as center point on map
        CameraUpdate update =
                CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLat, defaultLong), defualtZoom); //set up camera settings
        mMap.moveCamera(update);                                                                    //update camera settings

        loadMarkers(MY_FILENAME);  //users markers

        //set up OnInfoWindowClickListener "note-map listeners must be set-up here"
        mMap.setOnInfoWindowClickListener(this);
    }
    /**
     * OnInfoWindowClick Listener will respond to user clicking on marker's data when begin
     * displayed.
     * When user selects destination google map app will open and start direction to desired
     * location.
     * -By Antonio Ramos
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //adds vibration feedback to marker windows
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        //.getPosition returns Latitude and longitude formatted in LatLng ex "36.534342,-87.354328"
        // need to convert to string
        String s = marker.getPosition().toString();
        try {
            String location = getIntent().getExtras().getString(s); //allows you to split contents from string
           if(location != null) {
               String[] tokens = location.split(",");
               longitude = Double.parseDouble(tokens[0]);
               latitude = Double.parseDouble(tokens[1]);
               name = marker.getTitle();

           }
            else
               Log.i("null ", "loc Null ****************");
        }catch (NullPointerException e){
            Log.i("null ", " ****************");

        }
        //once marker is selected directions to desired location will start using Intents
        Intent intent = new Intent(getApplicationContext(), Navigation.class);
        intent.putExtra("name",name);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("position", Integer.valueOf(marker.getSnippet()));
        startActivity(intent);
    }
}
