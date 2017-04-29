package com.antonioramos.classtracker1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.webkit.WebView;

/**
 * Created by antonio on 4/18/2017.
 */

public class Navigation extends AppCompatActivity{

    private String name;
    private double longitude;
    private double latitude;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.navigation_layout);


        Intent intent = getIntent();
        name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
        longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
        latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+ longitude+"&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);


    }
}
