package com.antonioramos.classtracker1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.MotionEvent;

import android.widget.ImageView;



/**
 * Created by antonio on 4/18/2017.
 */

public class Navigation extends AppCompatActivity {

    private String name;
    private double longitude;
    private double latitude;
    private int nextPicture =1;
    private int pictureSet;
    private int startPicture;


    int indoorPics[] = {R.drawable.one, R.drawable.two,R.drawable.three, R.drawable.four, R.drawable.five,
        R.drawable.six};
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.navigation_layout);

        Intent intent = getIntent();
        name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
        longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
        latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);

        pictureSet = intent.getIntExtra("position",0);

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+ longitude+"&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


        startPicture= pictureSet * 3;
        ImageView iv = (ImageView)findViewById(R.id.class_imageView);
        iv.setImageResource(indoorPics[startPicture]);

    }

    @Override
    public boolean onTouchEvent( MotionEvent motionEvent) {
        ImageView iv = (ImageView)findViewById(R.id.class_imageView);
        switch (motionEvent.getAction()){
          case MotionEvent.ACTION_DOWN:

              if(nextPicture % 3 != 0 ){
                  startPicture++;
                  iv.setImageResource(indoorPics[startPicture]);
                  nextPicture++;
             }
              else{
                  startPicture -= 2;
                  nextPicture -=2 ;
                  iv.setImageResource(indoorPics[startPicture]);
              }
          break;
      }

        return true;
    }
}
