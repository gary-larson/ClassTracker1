package com.antonioramos.classtracker1;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
created by Gary
class to display available locations and save new ones
 */
public class LocationListActivity extends ListActivity {

    private ArrayAdapter<MyLocation> adapter;
    private ArrayList<MyLocation> allLocations;
    private ArrayList<MyLocation> myLocations;
    public static final String MY_FILENAME = "mylocations.loc";
    public static final String DATA_FILENAME = "locations.loc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get master list
        allLocations = readData(DATA_FILENAME);
        // Get locations from file
        myLocations = readData(MY_FILENAME);

        for (MyLocation loc : myLocations) {
            allLocations.add(loc);
        }


        /*
        Created by Gary
        if location is passed in add it to the list and save list
         */

        Intent intent = getIntent();

        if (intent.hasExtra(CurrentLocationActivity.NAME_KEY)) {
            String name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
            double longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
            double latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);
            MyLocation myLocation = new MyLocation(name, longitude, latitude);
            myLocations.add(myLocation);
            allLocations.add(myLocation);
            writeData();
            Intent intent2 = new Intent(getApplicationContext(), TitleActivity.class);
            startActivity(intent2);
        }

        // Setup adapter
        adapter = new ArrayAdapter<MyLocation>(this,
                android.R.layout.simple_list_item_1,
                allLocations);

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
                intent.putExtra(CurrentLocationActivity.NAME_KEY, location.getName());
                intent.putExtra(CurrentLocationActivity.LONGITUDE_KEY, location.getLongitude());
                intent.putExtra(CurrentLocationActivity.LATITUDE_KEY, location.getLatitude());
                startActivity(intent);

                Log.i("this should work", "*********************************"+ location.getLatitude()+position);
            }
        });

    }




    /*
    Created by Gary
   method will retrieve ArrayList<Location> from file class return locations
   */
    private ArrayList<MyLocation> readData(String filename) {
        ArrayList<MyLocation> myLocations = new ArrayList<>();
        try {
            Log.i("READ", "entered read");
            if (filename.equals(MY_FILENAME)) {
                FileInputStream fis = openFileInput(MY_FILENAME);
                ObjectInputStream objectInStream = new ObjectInputStream(fis);
                myLocations =  (ArrayList) objectInStream.readObject();
                fis.close();
            } else {
                InputStreamReader isr = new InputStreamReader(getApplicationContext().getAssets().open(DATA_FILENAME));
                Scanner sc = new Scanner(isr).useDelimiter(",");
                while (sc.hasNext()) {
                    String s = sc.nextLine();
                    String[] tokens = s.split(",");
                    s = tokens[0];
                    double lon = Double.parseDouble(tokens[1]);
                    double lat = Double.parseDouble(tokens[2]);
                    MyLocation loc = new MyLocation(s, lon, lat);
                    myLocations.add(loc);
                }
                sc.close();
                isr.close();
            }

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

            FileOutputStream fis = openFileOutput(MY_FILENAME, Context.MODE_PRIVATE);
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