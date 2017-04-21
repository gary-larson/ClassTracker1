package com.antonioramos.classtracker1;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
created by Gary
class to display available locations and save new ones
 */
public class LocationListActivity extends ListActivity {

    private ArrayAdapter<MyLocation> adapter;
    private ArrayList<MyLocation> myLocations;
    public static final String DATA_FILENAME = "locations.txt";

    public static final String LONGITUDE_KEY = "longitude";
    public static final String LATITUDE_KEY = "latitude";
    public static final String NAME_KEY = "name";
    private String name;
    private double latitude,longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get locations from file
        myLocations = readData();

        /*
        Created by Gary
        if location is passed in add it to the list and save list
         */

        Intent intent = getIntent();

        if (intent.hasExtra(CurrentLocationActivity.NAME_KEY)) {
            name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
            longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
            latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);
            MyLocation myLocation = new MyLocation(name, longitude, latitude);
            myLocations.add(myLocation);
            writeData();
            Intent intent2 = new Intent(getApplicationContext(), TitleActivity.class);
            startActivity(intent2);
        }

        // Setup adapter
        adapter = new ArrayAdapter<MyLocation>(this,
                android.R.layout.simple_list_item_1,
                myLocations);

        setListAdapter(adapter);

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                MyLocation location =(MyLocation)adapter.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), Navigation.class);
                // add variables to intent and start it
                intent.putExtra(NAME_KEY, location.getName());
                intent.putExtra(LONGITUDE_KEY, location.getLongitude());
                intent.putExtra(LATITUDE_KEY, location.getLatitude());
                startActivity(intent);

                Log.i("this should work", "*********************************"+ location.getLatitude()+position);
            }
        });

    }

    /*
    Created by Gary
   method will retrieve ArrayList<Location> from file class return locations
   */
    private ArrayList<MyLocation> readData() {
        ArrayList<MyLocation> myLocations = new ArrayList<>();
        try {
            Log.i("READ", "entered read");
            FileInputStream fis = openFileInput(DATA_FILENAME);
            ObjectInputStream objectInStream = new ObjectInputStream(fis);
            myLocations =  (ArrayList) objectInStream.readObject();

            fis.close();

        } catch (FileNotFoundException e) {
            // ok if file does not exist
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("READ", "Can not find Data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return myLocations;
    }

    /*
    Created by Gary
   method will retrieve ArrayList<Location> from Location class and save data to a file
   */
    private void writeData() {
        try {

            FileOutputStream fis = openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fis);
            objectOutputStream.writeObject(myLocations);
            fis.close();
            Log.e("writeData", "*************************************************** ");

        } catch (FileNotFoundException e) {
            Log.e("WRITE", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
            //Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WRITE", "Cannot save data: " + e.getMessage());
        }
    }
}